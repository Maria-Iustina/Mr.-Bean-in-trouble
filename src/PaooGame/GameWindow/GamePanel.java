package PaooGame.GameWindow;

import PaooGame.Input.Keyboard;
import PaooGame.Input.Mouse;
import PaooGame.Main.Game;

import javax.swing.*;
import java.awt.*;

import static PaooGame.Main.Game.GAME_HEIGHT;
import static PaooGame.Main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

    private Mouse mouse;
    private Game game;

    public GamePanel(Game game) {
        mouse = new Mouse(this);
        this.game = game;

        setPanelSize();
        setFocusable(true);
        requestFocusInWindow(); // <- asigură focusul
        addKeyListener(new Keyboard(this));
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }


    private void setPanelSize() {
        Dimension size=new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("size : "+ GAME_WIDTH + " : " + GAME_HEIGHT);
    }



    public void updateGame() {

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame()
    {
        return game;
    }

}