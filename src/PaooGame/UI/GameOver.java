package PaooGame.UI;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.GameState;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static PaooGame.Graphics.Constants.UI.Buttons.B_HEIGHT_DEFAULT;
import static PaooGame.Graphics.Constants.UI.Buttons.B_WIDTH_DEFAULT;

public class GameOver {

    private final Play play;
    private OtherButtons replayButton, menuButton;

    private BufferedImage gameOverImage;
    private int x, y, width, height;

    public GameOver(Play play) throws ResourceNotFoundException {

        this.play = play;
        createImage();
        createButtons();
    }

    private void createButtons() throws ResourceNotFoundException {
        int menuY = (int)(500 * Game.SCALE);
        int replayY = (int)(420 * Game.SCALE);
        int buttonX = (int)(725 * Game.SCALE);
        menuButton = new OtherButtons(buttonX, menuY, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT, 2);
        replayButton = new OtherButtons(buttonX, replayY, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT, 1);
    }

    private void createImage() throws ResourceNotFoundException {
        gameOverImage = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER_IMAGE);
        width = (int)(Game.SCALE * gameOverImage.getWidth());
        height = (int)(Game.SCALE * gameOverImage.getHeight());
        x = Game.GAME_WIDTH/2 - width/2;
        y = (int)(100 * Game.SCALE);
    }

    public void update(){

        menuButton.update();
        replayButton.update();

    }

    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(gameOverImage, x, y, width, height, null);
        menuButton.draw(g);
        replayButton.draw(g);
    }


    public void keyPressed(KeyEvent e) throws ResourceNotFoundException {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            play.resetAll();
            GameState.state = GameState.MENU;
        }
    }

    public void mouseMoved(MouseEvent e){
        replayButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if(isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if(isIn(e, replayButton))
            replayButton.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) throws ResourceNotFoundException {
        if(isIn(e, menuButton))
            if(menuButton.isMousePressed)
            {
                play.resetAll();
                GameState.state = GameState.MENU;
            }
        if(isIn(e, replayButton))
            if(replayButton.isMousePressed){
                play.resetAll();
            }

        menuButton.resetBooleans();
        replayButton.resetBooleans();

    }

    public void mousePressed(MouseEvent e){
        if(isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if(isIn(e, replayButton))
            replayButton.setMousePressed(true);
    }

    private boolean isIn(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
