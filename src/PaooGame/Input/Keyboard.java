package PaooGame.Input;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameWindow.GamePanel;
import PaooGame.GameStates.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static PaooGame.GameStates.GameState.PLAY;
    import PaooGame.GameWindow.GamePanel;

public class Keyboard implements KeyListener {
    private GamePanel gamePanel;

    public Keyboard(GamePanel gamePanel) {
        this.gamePanel=gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAY:
                try {
                    gamePanel.getGame().getPlaying().keyPressed(e);
                } catch (ResourceNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            default:
                break;
        }


          /*  case KeyEvent.VK_SPACE:   BUTONUL PT ATAC INCA NU ESTE IN VIDEO
                gamePanel.setAttacking(true);
                break;*/

    }

    @Override
    public void keyReleased(KeyEvent e) {       // dupa ce eliberam o tasta jucatorul nu se mai misca
                switch (GameState.state) {
                    case MENU:
                        gamePanel.getGame().getMenu().keyReleased(e);
                        break;
                    case PLAY:
                        gamePanel.getGame().getPlaying().keyReleased(e);
                        break;
                    default:
                        break;
                }
        }
    }
