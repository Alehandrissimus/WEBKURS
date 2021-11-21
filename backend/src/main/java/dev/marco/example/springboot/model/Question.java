package dev.marco.example.springboot.model;

import dev.marco.example.springboot.model.impl.AnswerImpl;

import java.math.BigInteger;
import java.util.List;

public interface Question {

    void setId(BigInteger id);

    BigInteger getId();

    void setQuestion(String question);

    String getQuestion();

    void setAnswers(List<AnswerImpl> answers);

    List<AnswerImpl> getAnswers();

    void setQuestionType(QuestionType questionType);

    QuestionType getQuestionType();
}
