package PaooGame.UI;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.Graphics.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static PaooGame.Graphics.Constants.UI.Buttons.*;

public class OtherButtons  extends PauseButton implements ButtonMethods {

        private List<List<BufferedImage>> images;
        private int index;

        public OtherButtons(int x, int y, int width, int height, int rowIndex) throws ResourceNotFoundException {
            super(x, y, width, height);
            this.rowIndex = rowIndex;
            loadImages();
        }

        private void loadImages() throws ResourceNotFoundException {
            BufferedImage buttons = LoadSave.GetSpriteAtlas(LoadSave.OTHER_BUTTONS);
            images = new ArrayList<>();

            for (int i = 0; i < 5; ++i) {
                List<BufferedImage> image = new ArrayList<>();
                for(int j = 0; j < 3; ++j) {
                    image.add(buttons.getSubimage(j * B_WIDTH_DEFAULT, i * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT));
                }
                images.add(image);
            }
        }

        public void update(){
            index = 0;
            if(isMouseOver)
                index = 1;
            if(isMousePressed)
                index = 2;
        }

        public void draw(Graphics g){
            g.drawImage(images.get(rowIndex).get(index), x, y, B_WIDTH, B_HEIGHT, null);
        }

        public void resetBooleans(){
            isMouseOver = false;
            isMousePressed = false;
        }

        public boolean isMousePressed() {
            return isMousePressed;
        }

        public void setMouseOver(boolean isMouseOver) {
            this.isMouseOver = isMouseOver;
        }
        public void setMousePressed(boolean isMousePressed) {
            this.isMousePressed = isMousePressed;
        }
}
