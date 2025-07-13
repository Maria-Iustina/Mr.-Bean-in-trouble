package PaooGame.Main;

import PaooGame.Entities.Player;
import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.GameState;
import PaooGame.GameStates.Play;
import PaooGame.GameWindow.GamePanel;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Levels.LevelManager;
import PaooGame.GameStates.Menu;

import java.awt.*;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET=120;
    private final int UPS_SET=200;

    private Play playing;
    private Menu menu;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1f;
    public final static int TILES_IN_WIDTH = 50;
    public final static int TILES_IN_HEIGHT = 25;
    public final static int TILES_SIZE=(int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() throws ResourceNotFoundException {
        initClasses();
        gamePanel= new GamePanel(this) ;
        gameWindow= new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() throws ResourceNotFoundException {
        menu = new Menu(this);
        playing = new Play(this);
    }

    private void startGameLoop(){
        gameThread=new Thread(this);
        gameThread.start();
    }
    public void update() throws ResourceNotFoundException {
        switch (GameState.state)
        {
            case PLAY:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
            case LOAD:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g)
    {
        switch (GameState.state)
        {
            case PLAY:
                playing.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
//            case LOAD:
//                break;
//            case QUIT:
            default:
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame= 1000000000.0/FPS_SET;
        double timePerUpdate=1000000000.0/UPS_SET;
        long previousTime=System.nanoTime();

        int frames=0;
        int updates=0;
        long lastCheck=System.currentTimeMillis();

        double deltaU=0;
        double deltaF=0;

        while(true)
        {

            long currentTime=System.nanoTime();

            deltaU+=(currentTime-previousTime)/timePerUpdate;
            deltaF+=(currentTime-previousTime)/timePerFrame;
            previousTime=currentTime;

            if(deltaU >= 1){
                try {
                    update();
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis()-lastCheck >= 1000)
            {
                lastCheck=System.currentTimeMillis();
                System.out.println("FPS: "+ frames+" | UPS: "+updates);
                frames=0;
                updates=0;
            }
        }
    }

    public Player getPlayer() {
        return playing.getPlayer();
    }

    public void windowsFocusLost()
    {
        //player.resetDirBooleans();
        if (GameState.state == GameState.PLAY)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu()
    {
        return menu;
    }

    public Play getPlaying()
    {
        return playing;
    }
}
