package com.mongodb.starter.controllers;

import com.mongodb.starter.models.Person;
import com.mongodb.starter.models.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Arrays.asList;
import com.mongodb.starter.repositories.QuestionRepository;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostMapping("question")
    @ResponseStatus(HttpStatus.CREATED)
    public Question postPerson(@RequestBody Question question) {
        return questionRepository.save(question);
    }

    @PostMapping("question")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Question> postPersons(@RequestBody List<Question> questions) {
        return questionRepository.saveAll(questions);
    }

    @GetMapping("question")
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("question/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable String id) {
        Question question = questionRepository.findOne(id);
        if (question == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(question);
    }

    @GetMapping("questions/{ids}")
    public List<Question> getQuestions(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return questionRepository.findAll(listIds);
    }

    @GetMapping("question/count")
    public Long getCount() {
        return questionRepository.count();
    }

    @DeleteMapping("question/{id}")
    public Long deleteQuestion(@PathVariable String id) {
        return questionRepository.delete(id);
    }

    @DeleteMapping("questions/{ids}")
    public Long deleteQuestions(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return questionRepository.delete(listIds);
    }

    @DeleteMapping("questions")
    public Long deleteQuestions() {
        return questionRepository.deleteAll();
    }

    @PutMapping("question")
    public Question putQuestion(@RequestBody Question question) {
        return questionRepository.update(question);
    }

    @PutMapping("questions")
    public Long putQuestion(@RequestBody List<Question> questions) {
        return questionRepository.update(questions);
    }

}
