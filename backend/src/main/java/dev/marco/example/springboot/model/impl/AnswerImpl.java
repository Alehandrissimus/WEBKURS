package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.model.Answer;
import dev.marco.example.springboot.model.AnswerResult;

import java.math.BigInteger;

public class AnswerImpl implements Answer {

    private BigInteger id;
    private String value;
    private AnswerResult answer;
    private BigInteger questionId;

    private AnswerImpl() {
    }

    public AnswerImpl(String value, AnswerResult answer, BigInteger questionId) {
        this.value = value;
        this.answer = answer;
        this.questionId = questionId;
    }

    public AnswerImpl(BigInteger id, String value, AnswerResult answer, BigInteger questionId) {
        this.id = id;
        this.value = value;
        this.answer = answer;
        this.questionId = questionId;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    @Override
    public AnswerResult getAnswer() {
        return answer;
    }

    @Override
    public BigInteger getQuestionId() {
        return questionId;
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setAnswer(AnswerResult answer) {
        this.answer = answer;
    }

    @Override
    public void setQuestionId(BigInteger questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "AnswerImpl{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", answer=" + answer +
                ", questionId=" + questionId +
                '}';
    }
}
