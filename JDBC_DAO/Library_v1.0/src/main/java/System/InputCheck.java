package System;

import System.Interfaces.CheckChoice;

public class InputCheck implements CheckChoice {

    @Override
    public boolean isCorrect(String input, int maxNum) {
     try {
          int num = Integer.parseInt(input);
          return num>0 && num<=maxNum;

     }catch (NumberFormatException e){
         return false;
     }
    }
}
