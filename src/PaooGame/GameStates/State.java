package PaooGame.GameStates;

import PaooGame.Main.Game;
import PaooGame.UI.MenuButtons;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game)
    {
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }

    public boolean isIn(MouseEvent e, MenuButtons mb)
    {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
}
