package dev.marco.example.springboot.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.marco.example.springboot.model.*;
import dev.marco.example.springboot.model.impl.*;
import dev.marco.example.springboot.service.GameService;
import dev.marco.example.springboot.service.QuizService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {

    private static final Logger log = Logger.getLogger(QuizControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private GameService gameService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void showAllQuizzesTest() throws Exception {

        when(quizService.getAllQuizzes())
                .thenReturn(
                        Arrays.asList(
                                QuizImpl.QuizBuilder()
                                        .setId(BigInteger.valueOf(1))
                                        .setTitle("Quiz1")
                                        .setDescription("Quiz1")
                                        .setQuizType(QuizType.MATHEMATICS)
                                        .setCreatorId(BigInteger.valueOf(1))
                                        .build(),
                                QuizImpl.QuizBuilder()
                                        .setId(BigInteger.valueOf(2))
                                        .setTitle("Quiz2")
                                        .setDescription("Quiz2")
                                        .setQuizType(QuizType.HISTORIC)
                                        .setCreatorId(BigInteger.valueOf(4))
                                        .build()
                        )
                );

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quiz/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id").value(BigInteger.valueOf(1)))
                .andExpect(jsonPath("$[0].title").value("Quiz1"))

                .andExpect(jsonPath("$[1].id").value(BigInteger.valueOf(2)))
                .andExpect(jsonPath("$[1].title").value("Quiz2"));

        verify(quizService).getAllQuizzes();
    }


    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void searchQuizzesLikeTitleTest() throws Exception {
        String title = "qu";
        Pageable pageable = PageRequest.of(1, 8);
        when(quizService.getQuizzesLikeTitle(1, title))
                .thenReturn(
                        new PageImpl<>(Arrays.asList(
                                QuizImpl.QuizBuilder()
                                        .setId(BigInteger.valueOf(1))
                                        .setTitle("Quiz1")
                                        .setDescription("Quiz1")
                                        .setQuizType(QuizType.HISTORIC)
                                        .setCreatorId(BigInteger.valueOf(1))
                                        .build(),
                                QuizImpl.QuizBuilder()
                                        .setId(BigInteger.valueOf(2))
                                        .setTitle("Quiz2")
                                        .setDescription("Quiz2")
                                        .setQuizType(QuizType.HISTORIC)
                                        .setCreatorId(BigInteger.valueOf(1))
                                        .build()
                        ), pageable, 2));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quiz/search?page=1&title={title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(quizService).getQuizzesLikeTitle(1, title);
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuizByIdTest() throws Exception {
        when(quizService.getQuizById(BigInteger.valueOf(1)))
                .thenReturn(QuizImpl.QuizBuilder()
                        .setId(BigInteger.valueOf(1))
                        .setTitle("NewQuiz")
                        .setDescription("Description of NewQuiz")
                        .setCreatorId(BigInteger.valueOf(3))
                        .setQuizType(QuizType.MATHEMATICS)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quiz/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BigInteger.ONE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("NewQuiz"));

    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void deleteQuizTest() throws Exception {

        when(quizService.getQuizById(BigInteger.valueOf(1)))
                .thenReturn(QuizImpl.QuizBuilder()
                        .setId(BigInteger.valueOf(1))
                        .setTitle("NewQuiz")
                        .setDescription("Description of NewQuiz")
                        .setCreatorId(BigInteger.valueOf(3))
                        .setQuizType(QuizType.MATHEMATICS)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/quiz/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void updateQuizTest() throws Exception {

        QuizImpl quiz = QuizImpl.QuizBuilder()
                .setId(BigInteger.valueOf(2))
                .setTitle("Testing your Light!!!")
                .setDescription("Light!!!")
                .setQuizType(QuizType.SCIENCE)
                .setCreationDate(new Date(System.currentTimeMillis()))
                .setCreatorId(BigInteger.valueOf(7))
                .build();

        when(quizService.getQuizById(BigInteger.valueOf(2)))
                .thenReturn(quiz);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/quiz/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quiz))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void buildNewQuizTest() throws Exception {

        List<QuestionImpl> questions = new ArrayList<>();
        QuestionImpl question = new QuestionImpl(BigInteger.valueOf(1), "Ukraine location?", QuestionType.TRUE_FALSE);

        List<AnswerImpl> answers = new ArrayList<>();
        answers.add(new AnswerImpl(BigInteger.valueOf(1), "America", AnswerResult.FALSE, question.getId()));
        answers.add(new AnswerImpl(BigInteger.valueOf(2), "Europe", AnswerResult.TRUE, question.getId()));

        question.setAnswers(answers);
        questions.add(question);

        Quiz quiz = QuizImpl.QuizBuilder()
                .setTitle("QuizBuild!")
                .setDescription("Light!")
                .setQuizType(QuizType.SCIENCE)
                .setCreatorId(BigInteger.valueOf(7))
                .setQuestions(questions)
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/quiz/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quiz))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(quizService).buildNewQuiz(any(Quiz.class));

    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void showAllFilterQuizzes() throws Exception {
        when(quizService.getQuizById(BigInteger.valueOf(1)))
                .thenReturn(QuizImpl.QuizBuilder()
                        .setId(BigInteger.valueOf(1))
                        .setTitle("NewQuiz")
                        .setDescription("Description of NewQuiz")
                        .setCreatorId(BigInteger.valueOf(3))
                        .setQuizType(QuizType.MATHEMATICS)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quiz/filter?id=1&filter=DATE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void quizGameTest() throws Exception {
        String quizName = "NewQuiz";

        when(gameService.sendGameQuiz(quizName))
                .thenReturn(QuizImpl.QuizBuilder()
                        .setId(BigInteger.valueOf(1))
                        .setTitle(quizName)
                        .setDescription("Desc of new quiz")
                        .setCreatorId(BigInteger.valueOf(7))
                        .setQuizType(QuizType.HISTORIC)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/quiz/game/{title}", quizName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BigInteger.ONE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(quizName));
    }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void finishQuizTest() throws Exception {
    UserImpl user = (UserImpl) new UserImpl.UserBuilder()
            .setId(BigInteger.valueOf(2036))
            .setEmail("JohnTitor@gmail.com")
            .setFirstName("John")
            .setLastName("Titor")
            .setDescription("Future")
            .build();
    List<AnswerImpl> userAnswers = new ArrayList<>();

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    this.mockMvc.perform(MockMvcRequestBuilders
            .post("/quiz/game/end")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new QuizController.ParamsInFinishQuiz(userAnswers, "ZNO", user)))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void setLikeOnQuizTest() throws Exception {
        QuizImpl quiz = QuizImpl.QuizBuilder()
                .setId(BigInteger.ONE)
                .setTitle("NewQuiz")
                .setDescription("Desc of NewQuiz")
                .setCreatorId(BigInteger.ONE)
                .build();
        QuizAccomplishedImpl quizAccomplished = new QuizAccomplishedImpl(5, false, quiz);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/quiz/like/{id}", BigInteger.ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quizAccomplished))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
