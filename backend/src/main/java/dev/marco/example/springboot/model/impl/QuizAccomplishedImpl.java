package dev.marco.example.springboot.model.impl;
import dev.marco.example.springboot.model.Quiz;

import java.util.Date;


public class QuizAccomplishedImpl {

  private int correctAnswers;
  private Boolean isFavourite;
  private Date dateOfQuiz;
  private QuizImpl quiz;

  public QuizAccomplishedImpl(int correctAnswers, Boolean isFavourite, QuizImpl quiz) {
    this.correctAnswers = correctAnswers;
    this.isFavourite = isFavourite;
    this.quiz = quiz;
  }

  public QuizAccomplishedImpl(int correctAnswers, Boolean isFavourite, Date dateOfQuiz, QuizImpl quiz) {
    this.correctAnswers = correctAnswers;
    this.isFavourite = isFavourite;
    this.dateOfQuiz = dateOfQuiz;
    this.quiz = quiz;
  }

  public QuizAccomplishedImpl(int correctAnswers, QuizImpl quiz) {
    this.isFavourite = false;
    this.correctAnswers = correctAnswers;
    this.quiz = quiz;
  }

  private QuizAccomplishedImpl() {
  }

  public int getCorrectAnswers() {
    return correctAnswers;
  }

  public void setCorrectAnswers(int correctAnswers) {
    this.correctAnswers = correctAnswers;
  }

  public Boolean getFavourite() {
    return isFavourite;
  }

  public void setBoolFavourite(int isFavourite) {
    if (isFavourite == 1) {
      this.isFavourite = true;
    } else if (isFavourite == 0) {
      this.isFavourite = false;
    }
  }

  public int getIntFavourite() {
    return this.isFavourite ? 1 : 0;
  }

  public void setFavourite(Boolean favourite) {
    isFavourite = favourite;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(QuizImpl quiz) {
    this.quiz = quiz;
  }

  public void setDateOfQuiz(Date dateOfQuiz) {
    this.dateOfQuiz = dateOfQuiz;
  }

  public Date getDateOfQuiz() {
    return dateOfQuiz;
  }

  @Override
  public String toString() {
    return "QuizAccomplishedImpl{" +
            "correctAnswers=" + correctAnswers +
            ", isFavourite=" + isFavourite +
            ", dateOfQuiz=" + dateOfQuiz +
            ", quiz=" + quiz +
            '}';
  }
}
