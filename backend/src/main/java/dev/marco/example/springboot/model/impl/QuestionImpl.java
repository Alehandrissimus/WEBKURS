package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.model.Question;
import dev.marco.example.springboot.model.QuestionType;

import java.math.BigInteger;
import java.util.List;

public class QuestionImpl implements Question {

    private QuestionImpl() {
    }

    public QuestionImpl(
            BigInteger idQuestion,
            String question,
            QuestionType questionType,
            List<AnswerImpl> answers
    ) {
        this.idQuestion = idQuestion;
        this.question = question;
        this.questionType = questionType;
        this.answers = answers;
    }

    public QuestionImpl(
            BigInteger idQuestion,
            String question,
            QuestionType questionType
    ) {
        this.idQuestion = idQuestion;
        this.question = question;
        this.questionType = questionType;
    }

    public QuestionImpl(
            String question,
            QuestionType questionType
    ) {
        this.question = question;
        this.questionType = questionType;
    }

    private BigInteger idQuestion;
    private String question;
    private QuestionType questionType;
    private List<AnswerImpl> answers;


    @Override
    public void setId(BigInteger id) {
        this.idQuestion = id;
    }

    @Override
    public BigInteger getId() {
        return idQuestion;
    }

    @Override
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public void setAnswers(List<AnswerImpl> answers) {
        this.answers = answers;
    }

    @Override
    public List<AnswerImpl> getAnswers() {
        return answers;
    }

    @Override
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    @Override
    public QuestionType getQuestionType() {
        return questionType;
    }

    @Override
    public String toString() {
        return "QuestionImpl{" +
                "idQuestion=" + idQuestion +
                ", question='" + question + '\'' +
                ", questionType=" + questionType +
                ", answers=" + answers +
                '}';
    }
}
