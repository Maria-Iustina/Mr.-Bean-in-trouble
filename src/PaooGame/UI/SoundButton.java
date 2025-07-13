package PaooGame.UI;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.Graphics.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static PaooGame.Graphics.Constants.UI.PauseButtons.SOUND_SIZE;

public class SoundButton  extends PauseButton implements ButtonMethods{

    private java.util.List<java.util.List<BufferedImage>> images;
    private boolean isMuted;
    public int colIndex;

    public SoundButton(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    public void loadImages(String imagePath, int rows, int cols) throws ResourceNotFoundException {
        BufferedImage buttons = LoadSave.GetSpriteAtlas(imagePath);
        images = new ArrayList<>();

        for (int j = 0; j < rows; ++j) {
            List<BufferedImage> image = new ArrayList<>();
            for (int i = 0; i < cols; ++i) {
                image.add(buttons.getSubimage(i * SOUND_SIZE, j * SOUND_SIZE, SOUND_SIZE, SOUND_SIZE));
            }
            images.add(image);
        }
    }

    public void update() {
        if (isMuted) {
            rowIndex = 1;
        } else rowIndex = 0;

        colIndex = 0;
        if (isMouseOver)
            colIndex = 1;
        if(isMousePressed) {
            colIndex = 2;
        }
    }

    public void draw(Graphics g){
        g.drawImage(images.get(rowIndex).get(colIndex), x, y, 64, 64,null);
    }

    public void resetBooleans(){
        isMouseOver = false;
        isMousePressed = false;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }
    public boolean isMuted() {
        return isMuted;
    }
    public void setMouseOver(boolean isMouseOver) {
        this.isMouseOver = isMouseOver;
    }
    public void setMuted(boolean isMuted) {
        this.isMuted = isMuted;
    }
    public void setMousePressed(boolean isMousePressed) {
        this.isMousePressed = isMousePressed;
    }
}