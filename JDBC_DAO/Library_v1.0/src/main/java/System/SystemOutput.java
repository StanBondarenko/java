package System;

import System.Interfaces.Output;
import java.util.Scanner;
import static java.lang.System.in;

public class SystemOutput implements Output {
    @Override
    public void print(String massage){
        System.out.println(massage);
    }
    @Override
    public void printError(String message){
        System.err.println("Error: "+message);
    }
}
