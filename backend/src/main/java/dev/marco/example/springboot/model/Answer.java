package dev.marco.example.springboot.model;

import java.math.BigInteger;

public interface Answer {
    String getValue();

    BigInteger getId();

    AnswerResult getAnswer();

    BigInteger getQuestionId();

    void setId(BigInteger id);

    void setValue(String value);

    void setAnswer(AnswerResult answer);

    void setQuestionId(BigInteger questionId);
}
