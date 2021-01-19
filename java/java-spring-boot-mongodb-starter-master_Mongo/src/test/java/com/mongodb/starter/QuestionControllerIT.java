package com.mongodb.starter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.starter.models.Question;
import com.mongodb.starter.repositories.QuestionRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestHelper testHelper;
    private String URL;

    @Autowired
    QuestionControllerIT(MongoClient mongoClient) {
        createQuestionCollectionIfNotPresent(mongoClient);
    }

    @PostConstruct
    void setUp() {
        URL = "http://localhost:" + port + "/api";
    }

    @AfterEach
    void tearDown() {
        questionRepository.deleteAll();
    }

    @DisplayName("POST /question with 1 question")
    @Test
    void postQuestion() {
        // GIVEN
        // WHEN
        ResponseEntity<Question> result = rest.postForEntity(URL + "/question", testHelper.getQ1(), Question.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Question questionResult = result.getBody();
        assertThat(questionResult.getId()).isNotNull();
        assertThat(questionResult).isEqualToIgnoringGivenFields(testHelper.getQ1(), "id", "createdAt");
    }

    @DisplayName("POST /question with 2 question")
    @Test
    void postQuestions() {
        // GIVEN
        // WHEN
        HttpEntity<List<Question>> body = new HttpEntity<>(testHelper.getQuest());
        ResponseEntity<List<Question>> response = rest.exchange(URL + "/question", HttpMethod.
                POST, body, new ParameterizedTypeReference<List<Question>>() {
        });
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).usingElementComparatorIgnoringFields("id", "createdAt")
                                      .containsExactlyInAnyOrderElementsOf(testHelper.getQuest());
    }

    @DisplayName("GET /questions with 2 questions")
    @Test
    void getQuestions() {
        // GIVEN
        List<Question> questionInserted = questionRepository.saveAll(testHelper.getQuest());
        // WHEN
        ResponseEntity<List<Question>> result = rest.exchange(URL + "/question", HttpMethod.GET, null,
                                                            new ParameterizedTypeReference<List<Question>>() {
                                                            });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyInAnyOrderElementsOf(questionInserted);
    }

    @DisplayName("GET /question/{id}")
    @Test
    void getQuestionById() {
        // GIVEN
        Question questionInserted = questionRepository.save(testHelper.getQ2());
        String idInserted = questionInserted.getId();
        // WHEN
        ResponseEntity<Question> result = rest.getForEntity(URL + "/question/" + idInserted, Question.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(questionInserted);
    }

    @DisplayName("GET /questions/{ids}")
    @Test
    void getQuestionsByIds() {
        // GIVEN
        List<Question> questionInserted = questionRepository.saveAll(testHelper.getQuest());
        List<String> idsInserted = questionInserted.stream().map(Question::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        String url = URL + "/questions/" + String.join(",", idsInserted);
        ResponseEntity<List<Question>> result = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Question>>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyInAnyOrderElementsOf(questionInserted);
    }

    @DisplayName("GET /questions/count")
    @Test
    void getCount() {
        // GIVEN
        questionRepository.saveAll(testHelper.getQuest());
        // WHEN
        ResponseEntity<Long> result = rest.getForEntity(URL + "/questions/count", Long.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
    }

    @DisplayName("DELETE /question/{id}")
    @Test
    void deleteQuestionById() {
        // GIVEN
        Question questionInserted = questionRepository.save(testHelper.getQ1());
        String idInserted = questionInserted.getId();
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/question/" + idInserted.toString(), HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(1L);
        assertThat(questionRepository.count()).isEqualTo(0L);
    }

    @DisplayName("DELETE /questions/{ids}")
    @Test
    void deleteQuestionsByIds() {
        // GIVEN
        List<Question> questionsInserted = questionRepository.saveAll(testHelper.getQuest());
        List<String> idsInserted = questionsInserted.stream().map(Question::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/question/" + String.join(",", idsInserted), HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
        assertThat(questionRepository.count()).isEqualTo(0L);
    }

    @DisplayName("DELETE /questions")
    @Test
    void deleteQuestions() {
        // GIVEN
        questionRepository.saveAll(testHelper.getQuest());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/questions", HttpMethod.DELETE, null,
                                                    new ParameterizedTypeReference<Long>() {
                                                    });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
        assertThat(questionRepository.count()).isEqualTo(0L);
    }

    @DisplayName("PUT /question")
    @Test
    void putQuestion() {
        // GIVEN
        Question questionInserted = questionRepository.save(testHelper.getQ1());
        // WHEN
        questionInserted.setQuestion("Trial");
        questionInserted.setCorrect_answers("Yes, I am");
        HttpEntity<Question> body = new HttpEntity<>(questionInserted);
        ResponseEntity<Question> result = rest.exchange(URL + "/question", HttpMethod.PUT, body, new ParameterizedTypeReference<Question>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(questionRepository.findOne(questionInserted.getId().toString()));
        assertThat(result.getBody().getQuestion()).isEqualTo("Trial");
        assertThat(result.getBody().getCorrect_answers()).isEqualTo("Yes, I am");
        assertThat(questionRepository.count()).isEqualTo(1L);
    }

    @DisplayName("PUT /questions with 2 questions")
    @Test
    void putQuestions() {
        // GIVEN
        List<Question> questionsInserted = questionRepository.saveAll(testHelper.getQuest());
        // WHEN
        questionsInserted.get(0).setQuestion("Trial");
        questionsInserted.get(0).setCorrect_answers("Yes, I am");
        questionsInserted.get(1).setQuestion("Trial2");
        questionsInserted.get(1).setCorrect_answers("No, I'm not");
        HttpEntity<List<Question>> body = new HttpEntity<>(questionsInserted);
        ResponseEntity<Long> result = rest.exchange(URL + "/questions", HttpMethod.PUT, body, new ParameterizedTypeReference<Long>() {
        });
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(2L);
        Question q1 = questionRepository.findOne(questionsInserted.get(0).getId().toString());
        Question q2 = questionRepository.findOne(questionsInserted.get(1).getId().toString());
        assertThat(q1.getQuestion()).isEqualTo("Trial");
        assertThat(q1.getCorrect_answers()).isEqualTo("Yes, I am");
        assertThat(q2.getQuestion()).isEqualTo("Trial2");
        assertThat(q2.getCorrect_answers()).isEqualTo("No, I'm not");
        assertThat(questionRepository.count()).isEqualTo(2L);
    }

    @DisplayName("GET /questions/averageAge")
    @Test
    void getAverageAge() {
        // GIVEN
        questionRepository.saveAll(testHelper.getQuest());
        // WHEN
        ResponseEntity<Long> result = rest.getForEntity(URL + "/questions/averageAge", Long.class);
        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(29L);
    }

    private void createQuestionCollectionIfNotPresent(MongoClient mongoClient) {
        // This is required because it is not possible to create a new collection within a multi-documents transaction.
        // Some tests start by inserting 2 documents with a transaction.
        MongoDatabase db = mongoClient.getDatabase("test2");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("questions"))
            db.createCollection("questions");
    }
}
