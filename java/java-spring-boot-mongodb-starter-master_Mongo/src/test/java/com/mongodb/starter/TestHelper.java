package com.mongodb.starter;

import com.mongodb.starter.models.Question;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
class TestHelper {

    Question getQ1() {
        Question q1 = new Question();
        List<String> incAns = new ArrayList<String>();;
        incAns.add("hey");
        incAns.add("feasf");
        incAns.add("fas");
        q1.setType("multiple");
        q1.setDifficulty("Easy");
        q1.setQuestion("Hola buenas quieres algo?");
        q1.setCorrect_answers("Nop");
        q1.setIncorrect_answers(incAns);
        return q1;
                
    }

    Question getQ2() {
        Question q1 = new Question();
        List<String> incAns = new ArrayList<String>();;
        incAns.add("MAL");
        incAns.add("PEOR");
        incAns.add("ESTA NOP");
        q1.setType("boolean");
        q1.setDifficulty("Hard");
        q1.setQuestion("Eres malo?");
        q1.setCorrect_answers("Si");
        q1.setIncorrect_answers(incAns);
        return q1;
    }
    
    List<Question> getQuest() {
        return asList(getQ1(), getQ2());
    }
}
