package System;

import System.Interfaces.Output;

public class Application {
    LibraryController controller = new LibraryController();
    public static void main(String[] str){
    Application app = new Application();
    app.run();
    }
    public void run(){
        controller.mainMenuNavigation();
    }
}
