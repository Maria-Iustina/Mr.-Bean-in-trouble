package PaooGame.UI;


import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.GameState;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.Constants;
import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static PaooGame.Graphics.Constants.UI.Buttons.B_HEIGHT_DEFAULT;
import static PaooGame.Graphics.Constants.UI.Buttons.B_WIDTH_DEFAULT;


public class CompletedLevel {

    private final Play play;
    private OtherButtons menuButton, nextButton;

    private BufferedImage completedLevelImage;
    private int x, y, width, height;

    public CompletedLevel(Play play) throws ResourceNotFoundException {
        this.play = play;

        loadBackground();
        createButtons();
    }

    private void loadBackground() throws ResourceNotFoundException {
        completedLevelImage = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_LEVEL_IMAGE);
        width = (int) (completedLevelImage.getWidth() * Game.SCALE);
        height = (int) (500 * Game.SCALE);
        x = Game.GAME_WIDTH / 2 - width / 2;
        y = (int) (90 * Game.SCALE);
    }

    private void createButtons() throws ResourceNotFoundException {
        int menuY = (int) (420 * Game.SCALE);
        int nextY = (int) (350 * Game.SCALE);
        int buttonX = (int) (720 * Game.SCALE);
        menuButton = new OtherButtons(buttonX, menuY, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT, 2);
        nextButton = new OtherButtons(buttonX, nextY, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT, 3);
    }

    public void update() {

        menuButton.update();
        nextButton.update();

    }

    public void draw(Graphics g) {
        g.drawImage(completedLevelImage, x, y, width, height, null);
        nextButton.draw(g);
        menuButton.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        nextButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, nextButton))
            nextButton.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) throws ResourceNotFoundException {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed) {
                play.resetAll();
                GameState.state = GameState.MENU;
            }
        }
        if (isIn(e, nextButton)) {
            if (nextButton.isMousePressed) {
                play.loadNextLevel();}

                menuButton.resetBooleans();
                nextButton.resetBooleans();

            }
        }

    public void mousePressed (MouseEvent e){
        if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, nextButton))
            nextButton.setMousePressed(true);
    }

    private boolean isIn (MouseEvent e, PauseButton button){
        return button.getBounds().contains(e.getX(), e.getY());
    }
}

