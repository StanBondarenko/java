package System.Interfaces;

public interface CheckChoice {
    boolean isCorrectNavigation(String input, int maxNum);
    boolean isCorrectDateFormat(String date);
    boolean isCorrectInt(String num);
    boolean isYesOrNo(String message);

}
