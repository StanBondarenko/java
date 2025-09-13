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
    @Override
    public void printMenu(){
        System.out.println("\uD83D\uDCDA ✏\uFE0F \uD83D\uDCBB Welcome to the ant library \uD83D\uDCDA ✏\uFE0F \uD83D\uDCBB");
        System.out.println("""
                1. Book navigation menu.
                2. Reader navigation menu.
                3. Service menu.
                4. Exit.""");

    }
    @Override
    public void printLogo(){
        System.out.println("""
                        .....                    .....         \s
                           ..:.                .:..            \s
                              ::              :.               \s
                               .-.          :-                 \s
                                .-.   ..   ::                  \s
                                  :::=++-::.       ..          \s
                                  .:=**+-::-.     ..           \s
                                 .:--++:-.:=.  .:..            \s
                        ..::.-:  .=-=*=-+==:  :-.              \s
                 .......      .-:  :-:::=--.  --               \s
                               -=..:. :::-: --  .:.            \s
                          .:-.  ::..:..-=::.. .-=:.-:          \s
                         :: .-=:   :-::-=:.  :=:.   --         \s
                        :-    .::..::::::.:....      --        \s
                       :-   .......::...::...:----:  ..:.      \s
                     .:.  .::::-:.....:-:::-:.    :-    ::.    \s
                    .:.   -:      .::-+*+:.:==.   :=     ...:. \s
                 .::.     =.     .:.-====-...:=:  .=:          \s
                ..       :-      .::=+***=:.:..:  .-=          \s
                         :.      .:.-=++=-::- .-   .:.         \s
                        :.        .::=++=:.:. :=    .-         \s
                       ::          .:-==-::.  :=     ::        \s
                     .:.             .:::.     -.     .:       \s
                   ...                         ::      .:..    \s
                   .                            ::       ..    \s
                                                 ::.           \s
                                                  .:           \s""");
    }
}
