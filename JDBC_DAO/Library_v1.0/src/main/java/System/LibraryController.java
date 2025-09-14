package System;

import System.Interfaces.CheckChoice;
import System.Interfaces.Input;
import System.Interfaces.Output;

public class LibraryController {
    Output out = new SystemOutput();
    Input input = new SystemInput();
    CheckChoice check = new InputCheck();

    public void mainMenuNavigation(){
        out.printLogo();
        boolean isMain = true;
        while (isMain) {
            boolean isSubmain= true;
            while (isSubmain) {
                String userNum = null;
                int numOfString=out.printMainMenu();
                userNum = input.promtChoice("Enter number>>>>");
                if (check.isCorrect(userNum, numOfString)) {
                    isSubmain=false;
                    isMain= navigationMain(userNum);
                } else {
                    out.printError("❌ ERROR: Invalid input!");
                    isSubmain = false;
                }
            }
        }
    }

    private boolean navigationMain(String userInput){
        boolean isSomethingElse= true;
        switch (userInput){
            case "1" ->{
                int numOfString = out.printBookMenu();
                String choice = input.promtChoice("Enter number>>>>");
                if(check.isCorrect(choice,numOfString)){

                }else {
                    out.printError("❌ ERROR: Invalid input!");
                }
            }
            case "2"->{

            }
            case "3"->{

            }
            case  "4"->{
                isSomethingElse=false;
            }
        }
        return isSomethingElse;
    }

//    private boolean navigationBook(){
//
//    }

}
