package System;

import System.Interfaces.CheckChoice;

public class InputCheck implements CheckChoice {

    @Override
    public boolean isCorrect(String input, int maxNum) {
        if(input.matches("[A-Za-z\\s]+") || input.isEmpty()){
            return false;
        }else {
            return Integer.parseInt(input) <= maxNum && Integer.parseInt(input) >= 0;
        }
    }
}
