package PaooGame.UI;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.GameState;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.DataBase;
import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static PaooGame.Graphics.Constants.UI.Buttons.B_HEIGHT;
import static PaooGame.Graphics.Constants.UI.Buttons.B_WIDTH;

public class Pause {
    private final Play play;
    private SoundButton soundButton, musicButton;
    private OtherButtons menuButton, replayButton, continueButton, saveButton;

    private BufferedImage pauseImage;

    private int pauseX, pauseY, pauseWidth, pauseHeight;

    public Pause(Play play) throws ResourceNotFoundException {
        this.play = play;
        loadBackground();
        createSoundButtons();
        createButtons();
    }

    private void createButtons() throws ResourceNotFoundException {
        int continueY = (int)(270 * Game.SCALE);
        int replayY = (int)(330 *  Game.SCALE);
        int saveY = (int)(390 *  Game.SCALE);
        int menuY = (int)(450 *  Game.SCALE);
        int buttonX = (int)(720 *  Game.SCALE);
        saveButton = new OtherButtons(buttonX, saveY, B_WIDTH, B_HEIGHT, 4);
        menuButton = new OtherButtons(buttonX, menuY, B_WIDTH, B_HEIGHT, 2);
        replayButton = new OtherButtons(buttonX, replayY, B_WIDTH, B_HEIGHT, 1);
        continueButton = new OtherButtons(buttonX, continueY, B_WIDTH, B_HEIGHT, 0);
    }

    private void createSoundButtons() throws ResourceNotFoundException {
        int soundX = (int)(690* Game.SCALE);
        int soundY = (int)(200* Game.SCALE);
        int musicX = (int)(830* Game.SCALE);
        int musicY = (int)(200* Game.SCALE);
        soundButton = new SoundButton(soundX, soundY, 64, 64);
        musicButton = new SoundButton(musicX, musicY, 64, 64);

        soundButton.loadImages(LoadSave.SOUND_BUTTONS, 2,3);
        musicButton.loadImages(LoadSave.MUSIC_BUTTONS, 2,3);
    }

    private void loadBackground() throws ResourceNotFoundException {
        pauseImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        pauseWidth = (int)(pauseImage.getWidth() * Game.SCALE);
        pauseHeight = (int)(500 * Game.SCALE);
        pauseX = Game.GAME_WIDTH / 2 - pauseWidth / 2;
        pauseY = (int)(90* Game.SCALE);
    }

    public void update(){
        musicButton.update();
        soundButton.update();
        saveButton.update();

        menuButton.update();
        replayButton.update();
        continueButton.update();
    }

    public void draw(Graphics g){
        // Background
        g.drawImage(pauseImage, pauseX, pauseY, pauseWidth, pauseHeight, null);

        // SoundButtons
        musicButton.draw(g);
        soundButton.draw(g);

        //OtherButtons
        menuButton.draw(g);
        replayButton.draw(g);
        continueButton.draw(g);
        saveButton.draw(g);
    }

    public void resetBooleans() {
        musicButton.resetBooleans();
        soundButton.resetBooleans();
        menuButton.resetBooleans();
        replayButton.resetBooleans();
        continueButton.resetBooleans();
        saveButton.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton)){
            musicButton.setMousePressed(true);
        } else if(isIn(e, soundButton))
            soundButton.setMousePressed(true);
        else if(isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if(isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if(isIn(e, continueButton))
            continueButton.setMousePressed(true);
        else if(isIn(e, saveButton))
            saveButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) throws ResourceNotFoundException {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted()); // Fixed: toggle mute properly
        } else if (isIn(e, soundButton)) {
            if (soundButton.isMousePressed())
                soundButton.setMuted(!soundButton.isMuted()); // Fixed: toggle mute properly
        } else if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
                play.unpauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                // Fixed: Complete level restart with full health and reset score
                play.restartCurrentLevelFully();
                play.unpauseGame();
            }
        } else if (isIn(e, continueButton)) {
            if (continueButton.isMousePressed()) {
                play.unpauseGame();
            }
        } else if (isIn(e, saveButton)) {
            if (saveButton.isMousePressed()) {
                // Fixed: Save current game state and go to menu
                play.saveCurrentGameState();
                GameState.state = GameState.MENU;
                play.unpauseGame();
            }
        }
        resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        setMouseOver();

        if(isIn(e, musicButton)){
            musicButton.setMouseOver(true);
        } else if(isIn(e, soundButton))
            soundButton.setMouseOver(true);
        else if(isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if(isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else if(isIn(e, continueButton))
            continueButton.setMouseOver(true);
        else if(isIn(e, saveButton))
            saveButton.setMouseOver(true);
    }

    public void setMouseOver() {
        musicButton.setMouseOver(false);
        soundButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        continueButton.setMouseOver(false);
        saveButton.setMouseOver(false);
    }

    private boolean isIn(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}