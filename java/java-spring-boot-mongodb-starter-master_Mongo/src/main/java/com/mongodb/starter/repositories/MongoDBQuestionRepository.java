package com.mongodb.starter.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.starter.dtos.AverageAgeDTO;
import com.mongodb.starter.models.Person;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.ReturnDocument.AFTER;
import com.mongodb.starter.models.Question;
import static java.util.Arrays.asList;

@Repository
public class MongoDBQuestionRepository implements QuestionRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    @Autowired
    private MongoClient client;
    private MongoCollection<Question> questionCollection;

    @PostConstruct
    void init() {
        questionCollection = client.getDatabase("trivia").getCollection("question", Question.class);
    }

    @Override
    public Question save(Question question) {
        question.setId(new String());
        questionCollection.insertOne(question);
        return question;
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                questions.forEach(p -> p.setId(new String()));
                questionCollection.insertMany(clientSession, questions);
                return questions;
            }, txnOptions);
        }
    }

    @Override
    public List<Question> findAll() {
        return questionCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<Question> findAll(List<String> ids) {
        return questionCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public Question findOne(String id) {
        return questionCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return questionCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return questionCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> questionCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> questionCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public Question update(Question question) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return questionCollection.findOneAndReplace(eq("_id", question.getId()), question, options);
    }

    @Override
    public long update(List<Question> questions) {
        List<WriteModel<Question>> writes = questions.stream()
                                                 .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()), p))
                                                 .collect(Collectors.toList());
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> questionCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }
}
