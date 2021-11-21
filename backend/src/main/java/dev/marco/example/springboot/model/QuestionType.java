package dev.marco.example.springboot.model;

public enum QuestionType {
    FOUR_ANSWERS,
    TRUE_FALSE,
    TYPE_ANSWER,
    SEQUENCE;

    public static QuestionType convertToRole(int roleId) {
        switch (roleId) {
            case 0:
                return FOUR_ANSWERS;
            case 1:
                return TRUE_FALSE;
            case 2:
                return TYPE_ANSWER;
            case 3:
                return SEQUENCE;
            default:
                throw new IllegalArgumentException("Convert to role received invalid argument roleId = " + roleId);
        }
    }

}
