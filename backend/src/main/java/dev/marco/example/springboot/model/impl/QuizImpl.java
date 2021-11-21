package dev.marco.example.springboot.model.impl;

import org.apache.commons.lang3.StringUtils;
import dev.marco.example.springboot.exception.QuizException;
import dev.marco.example.springboot.model.Question;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.QuizType;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static dev.marco.example.springboot.exception.MessagesForException.*;

public class QuizImpl implements Quiz {

    private BigInteger id;
    private String title;
    private String description;
    private QuizType quizType;
    private Date creationDate;
    private BigInteger creatorId;
    private List<QuestionImpl> questions;

    private QuizImpl() {
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public QuizType getQuizType() {
        return quizType;
    }

    @Override
    public void setQuizType(QuizType quizType) {
        this.quizType = quizType;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public BigInteger getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(BigInteger creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public List<QuestionImpl> getQuestions() {
        return questions;
    }

    @Override
    public void setQuestions(List<QuestionImpl> questions) {
        this.questions = questions;
    }

    public static Builder QuizBuilder() {
        return new QuizImpl().new Builder();
    }

    @Override
    public String toString() {
        return "QuizImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", quizType=" + quizType +
                ", creationDate=" + creationDate +
                ", creatorId=" + creatorId +
                ", questions=" + questions +
                '}';
    }

    public class Builder {

        private Builder() {
        }

        public Builder setId(BigInteger id) throws QuizException {
            if(id == null)
                throw new QuizException(EMPTY_ID);
            QuizImpl.this.id = id;
            return this;
        }

        public Builder setTitle(String title) throws QuizException {
            if (StringUtils.isBlank(title))
                throw new QuizException(EMPTY_TITLE);
            QuizImpl.this.title = title;
            return this;
        }

        public Builder setDescription(String description) throws QuizException {
            if (StringUtils.isBlank(description))
                throw new QuizException(EMPTY_DESCRIPTION);
            QuizImpl.this.description = description;
            return this;
        }

        public Builder setQuizType(QuizType quizType) {
            QuizImpl.this.quizType = quizType;
            return this;
        }

        public Builder setCreationDate(Date creationDate) {
            QuizImpl.this.creationDate = creationDate;
            return this;
        }

        public Builder setCreatorId(BigInteger creatorId) throws QuizException {
            if (creatorId == null) {
                throw new QuizException(OWNER_IS_NULL);
            }
            QuizImpl.this.creatorId = creatorId;
            return this;
        }

        public Builder setQuestions(List<QuestionImpl> questions) {
            QuizImpl.this.questions = questions;
            return this;
        }

        public QuizImpl build() {
            return QuizImpl.this;
        }
    }

}
