package dev.marco.example.springboot.rest;

import dev.marco.example.springboot.model.impl.*;
import dev.marco.example.springboot.service.UserService;
import dev.marco.example.springboot.service.GameService;
import dev.marco.example.springboot.util.ApiAddresses;
import dev.marco.example.springboot.util.ControllerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.*;
import dev.marco.example.springboot.service.QuizService;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

import static dev.marco.example.springboot.exception.MessagesForException.*;
import static dev.marco.example.springboot.util.ApiAddresses.API_QUIZ;

@RestController
@RequestMapping(API_QUIZ)
public class QuizController implements ApiAddresses {

    private final QuizService quizService;
    private final UserService userService;
    private final GameService gameService;

    private static final Logger log = Logger.getLogger(QuizController.class);
    private final Properties properties = new Properties();

    @Autowired
    public QuizController(QuizService quizService, UserService userService, GameService gameService) {
        this.quizService = quizService;
        this.userService = userService;
        this.gameService = gameService;
        try {
            ControllerUtil.getProperty(properties);
        } catch (ControllerConfigException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(CONFIG_EXCEPTION));
        }
    }

//    public void setTestConnection() throws DAOConfigException {
//        quizService.setTestConnection();
//        userService.setTestConnection();
//        gameService.setTestConnection();
//    }

    @GetMapping(API_ALL_QUIZZES)
    public List<Quiz> showAllQuizzes() {
        try {
            return quizService.getAllQuizzes();
        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping
    public Page<Quiz> showQuizzesByPage(@RequestParam("page") int pageNumber) {
        try {
            return quizService.getQuizzesByPage(pageNumber);
        } catch (PageException e) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(API_GET_QUIZ_BY_TITLE)
    public Page<Quiz> getQuizzesLikeTitle(@RequestParam("page") int pageNumber,
                                          @RequestParam("title") String title) {
        try {
            return quizService.getQuizzesLikeTitle(pageNumber, title);
        } catch (PageException e) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(API_GET_QUIZ_BY_ID)
    public Quiz getQuizById(@PathVariable BigInteger id) {
        try {
            return quizService.getQuizById(id);
        } catch (QuizDoesNotExistException | QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/")
    public Quiz createQuiz(@RequestBody QuizImpl quiz) {
        try {
            return quizService.buildNewQuiz(quiz);
        } catch (QuestionException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserException e) {
            log.error(DONT_ENOUGH_RIGHTS + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AnswerDoesNotExistException e) {
            log.error(getAnswerByIdNotFoundExc + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (AnswerException e) {
            log.error(ANSWER_EXCEPTION);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(API_UPDATE_QUIZ)
    public Quiz updateQuiz(@PathVariable BigInteger id,
                           @RequestBody QuizImpl updatedQuiz) {
        try {
            quizService.updateQuiz(id, updatedQuiz);
            return updatedQuiz;

        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UserException e) {
            log.error(DONT_ENOUGH_RIGHTS + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(API_DELETE_QUIZ)
    public void deleteQuiz(@PathVariable BigInteger id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            quizService.deleteQuiz(quiz);
        } catch (QuizDoesNotExistException | QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UserException e) {
            log.error(DONT_ENOUGH_RIGHTS + e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(API_SHOW_ALL_FILTER_QUIZZES)
    public List<Quiz> showAllFilterQuizzes(@RequestParam BigInteger id, @RequestParam Filter filter) {
        try {
            return Filter.getQuzziesByFilter(filter, id, quizService, userService);
        } catch (UserDoesNotExistException e) {
            log.error(USERS_DOESNT_EXIT + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUIZ_EXCEPTION));
        }
    }

    @GetMapping(API_QUIZ_GAME)
    public Quiz quizGame(@PathVariable String title) {
        try {
            return gameService.sendGameQuiz(title);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (QuizDoesNotExistException | QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUIZ_EXCEPTION));
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUESTION_EXCEPTION));
        } catch (AnswerDoesNotExistException e) {
            log.error(ANSWER_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANSWER_EXCEPTION));
        }
    }

    @PostMapping(API_FINISH_QUIZ)
    public List<QuestionImpl> finishQuiz(@RequestBody ParamsInFinishQuiz params) {
        try {
            Quiz quiz = quizService.getQuizByTitle(params.quizTitle);
            return gameService.validateAnswers(quiz, params.user, params.answers);
        } catch (QuizDoesNotExistException | QuizException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUIZ_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (QuestionDoesNotExistException e) {
            log.error(QUESTION_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUESTION_EXCEPTION));
        } catch (AnswerDoesNotExistException e) {
            log.error(ANSWER_NOT_FOUND + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANSWER_EXCEPTION));
        }
    }

    @PutMapping(API_LIKE_QUIZ)
    public void setLikeOnQuiz(@PathVariable BigInteger id, @RequestBody QuizAccomplishedImpl quizAccomplished) {
        try {
            User user = userService.getUserById(id);
            gameService.setIsFavorite(user, quizAccomplished);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(USERS_DOESNT_EXIT + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    static class ParamsInFinishQuiz {
        List<AnswerImpl> answers;
        String quizTitle;
        UserImpl user;

        public ParamsInFinishQuiz(List<AnswerImpl> answers, String quizTitle, UserImpl user) {
            this.answers = answers;
            this.quizTitle = quizTitle;
            this.user = user;
        }
    }
}
