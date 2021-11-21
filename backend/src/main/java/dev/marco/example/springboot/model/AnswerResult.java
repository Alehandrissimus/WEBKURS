package dev.marco.example.springboot.model;

public enum AnswerResult {
    TRUE,
    FALSE,
    SELECTED;

    public static AnswerResult convertIntToAnswer(int answerResult) {
        switch (answerResult) {
            case 0:
                return FALSE;
            case 1:
                return TRUE;
            case 2:
                return SELECTED;
            default:
                throw new IllegalArgumentException("Convert to answer received invalid argument answerResult = " + answerResult);
        }
    }

    public static AnswerResult convertBooleanToAnswer(boolean answerResult) {
        if (!answerResult)
            return FALSE;
        else
            return TRUE;

    }
}
