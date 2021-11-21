package dev.marco.example.springboot.exception;

public interface MessagesForException {

  String ERROR_WHILE_ACTIVATING = "User already activated";
  String EMAIL_ERROR = "Error while sending email";
  String ERROR_WHILE_CONFIRMING_EMAIL = "Error while confirming email";
  String USER_ALREADY_EXIST = "User already exist";
  String PROPERTY_ERROR = "Property error";
  String MESSAGE_ERROR = "Message error";
  String ERROR_WHILE_RECOVERING_PASSWORD = "Error while recovering password";
  String WRONG_PASSWORD = "Wrong password!";

  String ERROR_WHILE_SETTING_TEST_CONNECTION = "Error while setting test connection ";
  String DAO_LOGIC_EXCEPTION = "Dao logic exception ";
  String EMPTY_ID = "Id cannot be empty";
  String EMPTY_TITLE = "Title field cannot be empty";
  String EMPTY_DESCRIPTION = "Description field cannot be empty";
  String TITLE_TOO_LONG = "Length of the title field cannot exceed 50 characters";
  String DESCRIPTION_TOO_LONG = "Length of the description field cannot exceed 300 characters";
  String USER_IS_NULL = "User field cannot be empty";
  String OWNER_IS_NULL = "Owner field cannot be empty";
  String LONG_TITLE = "Long title";
  String LONG_DESCRIPTION = "Long description";

  String ANNOUNCEMENT_NOT_FOUND_EXCEPTION = "Announcement does not exist!";
  String ANNOUNCEMENT_HAS_NOT_BEEN_RECEIVED = "Announcement has not been received";
  String EMPTY_ANNOUNCEMENT_ID = "IdAnnouncement field cannot be empty";
  String ANNOUNCEMENT_ALREADY_EXISTS = "Announcement with the same title already exists";
  String EMPTY_ANNOUNCEMENT_TITLE = "Title field cannot be empty";
  String EMPTY_ANNOUNCEMENT_DESCRIPTION = "Description field cannot be empty";
  String EMPTY_ANNOUNCEMENT_ADDRESS = "Address field cannot be empty";
  String ADDRESS_TOO_LONG = "Length of the address field cannot exceed 30 characters";

  String QUIZ_ALREADY_EXISTS = "Quiz with the same title already exists";
  String QUIZ_NOT_FOUND_EXCEPTION = "Quiz does not exist!";
  String QUIZ_HAS_NOT_BEEN_RECEIVED = "Quiz has not been received";
  String CREATE_QUIZ_EXCEPTION = "SQL Exception while createQuiz in QuizDAOImpl";
  String DELETE_QUIZ_EXCEPTION = "SQL Exception while deleteQuiz in QuizDAOImpl";
  String GET_QUIZ_BY_ID_EXCEPTION = "SQL Exception while getQuizById in QuizDAOImpl";
  String GET_ALL_QUIZZES_EXCEPTION = "SQL Exception while getAllQuizzes in QuizDAOImpl";
  String GET_LAST_CREATED_QUIZZES_EXCEPTION = "SQL Exception while getLastThreeCreatedQuizzes in QuizDAOImpl";
  String GET_QUIZ_BY_TITLE_EXCEPTION = "SQL Exception while getQuizByTitle in QuizDAOImpl";
  String GET_QUIZZES_BY_TYPE_EXCEPTION = "SQL Exception while getQuizzesByType in QuizDAOImpl";
  String ACCOMPLISHED_QUIZ_HAS_NOT_BEEN_FOUNDED = "Accomplished quiz has not been founded";
  String PAGE_DOES_NOT_EXIST = "Page does not exist";
  String QUESTION_NOT_FULL = "Should be 10 questions";
  String ANSWERS_NOT_FULL_FOR_FOURANSWER = "Should be 4 answers in ";
  String ANSWERS_NOT_FULL_FOR_TRUEFALSSE = "Should be 2 answers in ";
  String SAME_ANSWERS_EXCEPTION = "Same answers ";
  String SAME_QUESTIONS_EXCEPTION = "Same questions with name ";
  String EMPTY_ANSWER_EXCEPTION = "Empty answer in ";
  String EMPTY_QUESTION_EXCEPTION = "Empty question";

  String TEST_CONNECTION_ERR = "Error while setting test connection %s";
  String TEST_CONNECTION_EXC = "Error while setting test connection";
  String QUESTION_NOT_FOUND = "QuestionDoesNotExistException ";
  String QUESTION_DUPLICATE = "Question title duplicate within one quiz";
  String QUESTION_EMPTY = "Question title is empty";

  String ANSWER_EMPTY = "Answer title is empty";
  String ANSWER_NOT_FOUND = "AnswerDoesNotExistException ";

  String getAnswerByIdNotFoundErr = "AnswerDoesNotExistException in getAnswerById, answerId = %d";
  String getAnswerByIdNotFoundExc = "getAnswerById() not found answer by answerId = %d";
  String getAnswerByIdLogicErr = "SQL Exception while getAnswerById in AnswerDAOImpl";
  String getAnswerByIdLogicExc = "SQL Exception while getAnswerById with answerId = %d";
  String getLastAnswerIdByTitleNotFoundErr = "AnswerDoesNotExistException in getLastAnswerIdByTitle, title = %s";
  String getLastAnswerIdByTitleNotFoundExc = "getLastAnswerIdByTitle() does not found answer with title = %s";
  String getLastAnswerIdByTitleLogicErr = "SQL Exception while getLastAnswerIdByTitle in AnswerDAOImpl";
  String getLastAnswerIdByTitleLogicExc = "SQL Exception while getLastAnswerIdByTitle with title = %s";
  String createAnswerLogicExc = "SQL Exception while createAnswer in AnswerDAOImpl";
  String deleteAnswerLogicExc = "SQL Exception while deleteAnswer in AnswerDAOImpl";
  String updateAnswerLogicExc = "SQL Exception while updateAnswer in AnswerDAOImpl";
  String getAnswersByQuestionIdLogicErr = "SQL Exception while getAnswersByQuestionId in AnswerDAOImpl";
  String getAnswersByQuestionIdLogicExc = "SQL Exception while getAnswersByQuestionId with questionId = %d";

  String DONT_ENOUGH_RIGHTS = "You don't have enough rights";
  String ANNOUNCEMENT_ALREADY_LIKED = "The user has already liked this announcement";
  String ANNOUNCEMENT_HAS_NOT_LIKED = "User has not liked this announcement yet";
  String USER_NOT_FOUND_EXCEPTION = "User does not exist!";
  String USER_HAS_NOT_BEEN_RECEIVED = "User has not been received";
  String INVALID_USERS_EMAIL = "Invalid users email error ";
  String INVALID_USERS_ID = "Invalid users id";
  String ERROR_WHILE_CREATING_USER = "Error while creating user";
  String USERS_DOESNT_EXIT = "User doesnt exist";
  String EMPTY_USER_ID = "Empty user id";
  String NULL_FIRST_NAME = "Null first name";
  String EMPTY_FIRST_NAME = "First name field cannot be empty";
  String NULL_LAST_NAME = "Null last name";
  String EMPTY_LAST_NAME = "Last name field cannot be empty";
  String NULL_EMAIL = "Null email";
  String EMPTY_EMAIL = "Email field cannot be empty";
  String NULL_PASSWORD = "Null password";
  String EMPTY_PASSWORD = "Password field cannot be empty";
  String INVALID_USERS_FIRST_NAME = "Invalid users first name";
  String INVALID_USERS_LAST_NAME = "Invalid users last name";

  String INVALID_EMAIL = "Invalid user email";
  String INVALID_PASSWORD = "Invalid password";
  String DAO_CONFIG_EXCEPTION = "Dao config exception";
  String CONTROLLER_CONFIG_EXCEPTION = "Controller config exception";

  String JWT_AUTH_EXC = "JwtTokenFilter doFilter JwtAuthenticationException ";
  String JWT_TOKEN_INVALID = "JwtTokenFilter doFilter Invalid JWT Token ";

  String TEST_ERROR = "Error while testing ";
}
