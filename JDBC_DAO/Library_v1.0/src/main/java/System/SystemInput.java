package System;

import System.Interfaces.Input;

import java.util.Scanner;

public class SystemInput implements Input {
    private Scanner scr;
    public SystemInput(){this.scr.hasNext();}
    @Override
    public String getInput() {return scr.nextLine();}
}
