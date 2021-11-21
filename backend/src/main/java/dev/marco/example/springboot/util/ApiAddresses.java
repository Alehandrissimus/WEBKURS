package dev.marco.example.springboot.util;

public interface ApiAddresses {

  String API_REGISTER_USER = "/auth/local/register";
  String API_AUTHORIZE_USER = "/auth/local";
  String API_RECOVER_PASSWORD= "/auth/recover";
  String API_UPDATE_PASSWORD = "/updatePassword/{id}";
  String API_CONFIRM_EMAIL = "/confirm";
  String API_GET_USER = "/user/{idUser}";
  String API_GET_USER_BY_TOKEN = "/user/me";
  String API_DELETE_USER = "/user/{idUser}";
  String API_EDIT_USER = "/user/{idUser}";
  String API_GET_ACCOMPLISHED_QUIZZES_BY_USER ="/user/acc_quiz/{userId}";
  String API_GET_FAVORITE_QUIZZES_BY_USER = "/user/favorite/{id}";

  String API_QUIZ = "/quiz";
  String API_ALL_QUIZZES = "/all";
  String API_GET_QUIZ_BY_TITLE = "/search";
  String API_GET_QUIZ_BY_ID = "/{id}";
  String API_UPDATE_QUIZ = "/{id}";
  String API_DELETE_QUIZ = "/{id}";
  String API_SHOW_ALL_FILTER_QUIZZES = "/filter";
  String API_QUIZ_GAME = "/game/{title}";
  String API_FINISH_QUIZ = "/game/end";
  String API_LIKE_QUIZ = "/like/{id}";

  String API_GENERATE_DASHBOARD = "/dashboard/{id}";

  String API_ANNOUNCEMENT = "/announcement";
  String API_CREATE_ANNOUNCEMENT = "/create";
  String API_GET_ALL_ANNOUNCEMENT = "/all/{idUser}";
  String API_DELETE_ANNOUNCEMENT = "/delete";
  String API_UPDATE_ANNOUNCEMENT = "/update";
  String API_LIKE_ANNOUNCEMENT = "/like";
  String API_GET_COMMENTARIES = "/commentaries";
  String API_CREATE_COMMENTARY = "/commentaries";
  String API_SEARCH_ANNOUNCEMENT = "/search";
  String API_GET_ANNOUNCEMENTS_BY_PAGE = "/all";
  String API_GET_ANNOUNCEMENTS_BY_TITLE = "/search";

  String CONFIG_EXCEPTION = "CONFIG_EXCEPTION";
  String DAO_LOGIC_EXCEPTION = "DAO_LOGIC_EXCEPTION";
  String MAIL_INVALID = "MAIL_INVALID";
  String MAIL_EXCEPTION = "MAIL_EXCEPTION";
  String USER_EXCEPTION = "USER_EXCEPTION";
  String PASSWORD_INVALID = "PASSWORD_INVALID";
  String AUTHENTICATION_EXCEPTION = "AUTHENTICATION_EXCEPTION";
  String NAME_INVALID = "NAME_INVALID";
  String DESCRIPTION_INVALID = "DESCRIPTION_INVALID";
  String QUIZ_EXCEPTION = "QUIZ_EXCEPTION";
  String ANNOUNCEMENT_EXCEPTION = "ANNOUNCEMENT_EXCEPTION";
  String ANSWER_EXCEPTION = "ANSWER_EXCEPTION";
  String QUESTION_EXCEPTION = "QUESTION_EXCEPTION";
  String PAGE_EXCEPTION = "PAGE_EXCEPTION";
  String DONT_ENOUGH_RIGHTS = "DONT_ENOUGH_RIGHTS";
}
