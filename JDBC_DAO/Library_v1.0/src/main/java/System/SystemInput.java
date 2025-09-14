package System;

import System.Interfaces.Input;
import java.util.Scanner;
import static java.lang.System.in;

public class SystemInput implements Input {
    private final Scanner scr;
    public SystemInput(){this.scr = new Scanner(in);}
    @Override
    public String getInput() {return scr.nextLine();}
    @Override
    public String promtChoice(String message) {
        System.out.println(message);
        return scr.nextLine();
    }
}
