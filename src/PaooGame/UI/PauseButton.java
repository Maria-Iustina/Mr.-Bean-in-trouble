package PaooGame.UI;

import java.awt.*;

public class PauseButton {
    protected boolean isMouseOver, isMousePressed;
    protected int x, y, width, height, rowIndex;
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bounds = new Rectangle(x, y, width, height);
    }
    public Rectangle getBounds(){
        return bounds;
    }

}

