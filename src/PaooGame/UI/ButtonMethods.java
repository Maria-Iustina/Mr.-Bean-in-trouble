package PaooGame.UI;

import java.awt.*;

public interface ButtonMethods {

    void update();
    void draw(Graphics g);

    void setMouseOver(boolean mouseOver);
    void setMousePressed(boolean mousePressed);

    void resetBooleans();

    boolean isMousePressed();
}