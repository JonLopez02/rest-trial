package com.mongodb.starter.repositories;

import com.mongodb.starter.models.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository {

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    List<Question> findAll();

    List<Question> findAll(List<String> ids);

    Question findOne(String id);

    long count();

    long delete(String id);

    long delete(List<String> ids);

    long deleteAll();

    Question update(Question question);

    long update(List<Question> questions);

}
