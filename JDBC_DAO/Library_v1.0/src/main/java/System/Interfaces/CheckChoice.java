package System.Interfaces;

public interface CheckChoice {
    boolean isCorrectNavigation(String input, int maxNum);
    int checkIfAuthorIsNew(String input, int maxRow);
    boolean isCorrectDateFormat(String date);
    boolean isCorrectInt(String num);
    boolean isYesOrNo(String message);
    boolean isWordsOnly(String input);

}
