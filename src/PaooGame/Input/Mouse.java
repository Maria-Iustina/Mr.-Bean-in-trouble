package PaooGame.Input;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.GameState;
import PaooGame.GameWindow.GamePanel;
import PaooGame.Main.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
    private GamePanel gamePanel;

    public Mouse(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state) {
            case PLAY:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
//            case LOAD:
//                break;
//            case QUIT:
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case PLAY:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
//            case LOAD:
//                break;
//            case QUIT:
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case PLAY:
                try {
                    gamePanel.getGame().getPlaying().mouseReleased(e);
                } catch (ResourceNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
//            case LOAD:
//                break;
//            case QUIT:
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
       /* // TODO Auto-generated method stub
        switch (GameState.state) {
            case PLAY:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            default:
                break;

        }*/

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case PLAY:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
//            case LOAD:
//                break;
//            case QUIT:
            default:
                break;
        }
    }
}
