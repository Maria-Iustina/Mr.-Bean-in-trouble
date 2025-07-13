package PaooGame.GameStates;

import PaooGame.Exceptions.ResourceNotFoundException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {
    public void update() throws ResourceNotFoundException;
    public void draw(Graphics g);
    public void mouseClicked(MouseEvent e);
    public void mouseReleased(MouseEvent e) throws ResourceNotFoundException;
    public void mouseMoved(MouseEvent e);
    public void mousePressed(MouseEvent e);
    public void keyPressed(KeyEvent e) throws ResourceNotFoundException;
    public void keyReleased(KeyEvent e);

}
