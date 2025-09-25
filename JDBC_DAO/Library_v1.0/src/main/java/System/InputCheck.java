package System;

import System.Interfaces.CheckChoice;
import System.Interfaces.Input;
import System.Interfaces.Output;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputCheck implements CheckChoice {
    Output out = new SystemOutput();
    Input input = new SystemInput();

    @Override
    public boolean isCorrectNavigation(String input, int maxNum) {
     try {
          int num = Integer.parseInt(input);
          return num>0 && num<=maxNum;
     }catch (NumberFormatException e){

         return false;
     }
    }
    @Override
    public int checkIfAuthorIsNew(String input, int maxRow) {
        if(input.equalsIgnoreCase("new")){
            return 3;// author is new
        }
       try {
           Integer.parseInt(input);
           return 1;// author not new
       }catch (NumberFormatException e){
           out.printError("Invalid input.");
           return 0;// not correct
       }
    }
    @Override
    public boolean isCorrectDateFormat(String date){
        try {
            LocalDate publishDate= LocalDate.parse(date);
            return true;
        }catch (DateTimeParseException e){
            out.printError("Invalid date input format, use the format: (YYYY-MM-DD).");
            return false;
        }
    }
    @Override
    public boolean isCorrectInt(String num) {
        try {
            int num1=Integer.parseInt(num);
            return num1 > 0;
        }catch (NumberFormatException e){
            out.printError("Please enter a number");
            return false;
        }
    }
    @Override
    public boolean isYesOrNo(String message) {
        while (true){
        String choice = input.promtChoice( message+" Y/N >>>>");
        choice = choice.toLowerCase();
        switch (choice) {
            case "y", "yes" -> {
                return true;
            }
            case "n","no" -> {
                return false;
            }
            default -> {
                out.printError("Invalid input");
            }
        }
        }
    }
    @Override
    public boolean isWordsOnly(String input){
      if(input.matches("[a-zA-Z]+")){
          return true;
      }else {
          out.printError("Incorrect input");
          return false;
      }
    }
    @Override
    public boolean isCorrectPhoneNumber(String phoneNum) {
        String pattern = "^[0-9()+\\-\\s]+$";
        if (phoneNum != null && phoneNum.matches(pattern)){
            return true;
        }else {
            out.printError("Invalid format.");
            return false;
        }

    }
    @Override
    public boolean hasNotSpecialCharacters(String input) {
        if (input.matches("[a-zA-Z0-9\\s]+")){
            return true;
        }else {
            out.printError("Special characters cannot be used.");
            return false;
        }
    }
    @Override
    public boolean isCorrectEmail(String email) {
        if(email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
            return true;
        }else {
            out.printError("Incorrect input");
            return false;
        }
    }


}
