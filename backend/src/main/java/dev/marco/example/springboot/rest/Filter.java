package dev.marco.example.springboot.rest;

import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.exception.QuizDoesNotExistException;
import dev.marco.example.springboot.exception.UserDoesNotExistException;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.QuizType;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import dev.marco.example.springboot.service.QuizService;
import dev.marco.example.springboot.service.UserService;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public enum Filter {
  DATE,
  MATHEMATICS,
  HISTORIC,
  SCIENCE,
  GEOGRAPHICAL,
  COMPLETED,
  FAVORITES;

  public static List<Quiz> getQuzziesByFilter(Filter filter, BigInteger id, QuizService quizService,
      UserService userService)
      throws DAOLogicException, QuizDoesNotExistException, UserDoesNotExistException {
    List<Quiz> filterQuizzes = Collections.emptyList();
    switch (filter) {
      case DATE:
        filterQuizzes = quizService.getLastCreatedQuizzes(10);
        break;
      case MATHEMATICS:
        filterQuizzes = quizService.getQuizzesByType(QuizType.MATHEMATICS);
        break;
      case SCIENCE:
        filterQuizzes = quizService.getQuizzesByType(QuizType.SCIENCE);
        break;
      case HISTORIC:
        filterQuizzes = quizService.getQuizzesByType(QuizType.HISTORIC);
        break;
      case GEOGRAPHICAL:
        filterQuizzes = quizService.getQuizzesByType(QuizType.GEOGRAPHICAL);
        break;
      case COMPLETED:
        Set<QuizAccomplishedImpl> accomplisheds = userService
            .getAccomplishedQuizesByUser(id);
        for (QuizAccomplishedImpl quiz : accomplisheds) {
          filterQuizzes.add(quiz.getQuiz());
        }
        break;
      case FAVORITES:
        Set<QuizAccomplishedImpl> favorites = userService.getFavoriteQuizesByUser(id);
        for (QuizAccomplishedImpl quiz : favorites) {
          filterQuizzes.add(quiz.getQuiz());
        }
        break;
      default:
        throw new DAOLogicException("Wrong filter");
    }
    return filterQuizzes;
  }
}
