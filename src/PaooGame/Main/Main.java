package PaooGame.Main;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameWindow.GameWindow;

public class Main
{
    public static void main(String[] args) throws ResourceNotFoundException {
        Game paooGame = new Game();
        paooGame.run();
    }
}
//
