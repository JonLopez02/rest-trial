package com.mongodb.starter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class Question {

    @JsonSerialize(using = ToStringSerializer.class)
    private String id;
    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answers;
    private List<String> incorrect_answers;


    public String getId() {
        return id;
    }

    public Question(String id, String category, String type, String difficulty, String question, String correct_answers, List<String> incorrect_answers) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answers = correct_answers;
        this.incorrect_answers = incorrect_answers;
    }

    public Question() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    @Override
    public String toString() {
        return "Questions{" + "id=" + id + ", category=" + category + ", type=" + type + ", difficulty=" + difficulty + ", question=" + question + ", correct_answers=" + correct_answers + ", incorrect_answers=" + incorrect_answers + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question q = (Question) o;
        return Objects.equals(id, q.id) && Objects.equals(category,
                q.category) && Objects.equals(type,
                        q.type) && Objects
                        .equals(difficulty, q.difficulty) && Objects.equals(question, q.question) && Objects.equals(correct_answers,
                q.correct_answers) && Objects
                        .equals(incorrect_answers, q.incorrect_answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, type, difficulty, question, correct_answers, incorrect_answers);
    }

}
