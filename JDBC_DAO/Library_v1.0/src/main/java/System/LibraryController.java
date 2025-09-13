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
                out.printMenu();
                userNum = input.promtChoice("Enter number>>>>");
                if (check.isCorrect(userNum, 4)) {

                } else {
                    out.printError("❌ ERROR: Invalid input!");
                    isSubmain = false;
                }
            }
        }

    }

}
