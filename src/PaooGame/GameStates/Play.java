package PaooGame.GameStates;

import PaooGame.Entities.EnemyManager;
import PaooGame.Entities.Player;
import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.Graphics.DataBase;
import PaooGame.Graphics.LoadSave;
import PaooGame.Levels.Level;
import PaooGame.Levels.LevelManager;
import PaooGame.Main.Game;
import PaooGame.Objects.ObjectManager;
import PaooGame.UI.CompletedLevel;
import PaooGame.UI.GameOver;
import PaooGame.UI.Pause;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static PaooGame.Graphics.HelpMethods.IsSolid;
import static PaooGame.Main.Game.SCALE;

public class Play extends State implements StateMethods {
    private static Player player;
    private static DataBase dataBase;
    private static LevelManager levelManager;
    private Pause pause;
    private GameOver gameOver;
    private CompletedLevel completedLevelUI;
    private ObjectManager objectManager;

    private EnemyManager enemyManager;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean showLevelCompleted = false;
    private boolean levelCompleted = false;
    private boolean load=false;

    private int xLvlOffset;
    private int leftBorder=(int)(0.3 * Game.GAME_WIDTH);
    private int rightBorder=(int)(0.7 * Game.GAME_WIDTH);
    private int lvlTilesWide= LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset=lvlTilesWide -Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX=maxTilesOffset*Game.TILES_SIZE;

    private int deathTimer = 0;
    private static final int DEATH_DELAY = 180; // 3 secunde la 60 FPS

    private int score = 0;

    public Play(Game game) throws ResourceNotFoundException {
        super(game);
        initClasses();
    }
    public void loadGame(int levelIndex, int hearts, int score, int xPos, int yPos) throws ResourceNotFoundException {
        resetAll();
        levelManager.loadGame(levelIndex,hearts,score,xPos,yPos);
    }

    private void initClasses() throws ResourceNotFoundException {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);

        int spawnTileX = 5;
        int spawnTileY = 5;

        // Calculează poziția corectă pentru Y (poate coborî dacă nu e podea sub el)
        float spawnX = spawnTileX * Game.TILES_SIZE;
        float spawnY = spawnTileY * Game.TILES_SIZE;

        player = Player.getInstance(spawnX, spawnY, (int)(128 * SCALE),  (int)(128 * SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());

        dataBase=DataBase.getInstance();

        pause = new Pause(this);
        gameOver = new GameOver(this);
        completedLevelUI = new CompletedLevel(this);

        objectManager = new ObjectManager(this);

        if (objectManager.getDoors() != null && !objectManager.getDoors().isEmpty()) {
            player.setDoors(objectManager.getDoors());
        }

    }
    private float getSpawnYPosition(int tileX, int[][] lvlData) {
        for (int yTile = 0; yTile < lvlData.length; yTile++) {
            if (IsSolid(tileX * Game.TILES_SIZE, yTile * Game.TILES_SIZE, lvlData,getLevelManager().getLevelIndex())) {
                return (yTile - 1) * Game.TILES_SIZE;
            }
        }
        return 0;
    }
    public void increaseScore(int value) {
        score = +value;
        if (player != null) {
            player.setScore(score);
        }

    }

    @Override
    public void update() throws ResourceNotFoundException {
        if (!isPaused && !isGameOver && !showLevelCompleted) {
            if (!player.isDead()) {
                // Update normal
                levelManager.update();
                player.update();
                enemyManager.update();
                objectManager.update();
                checkCloseToBorder();

                // Verifică coliziunea cu inamicii (ex: albine)
                enemyManager.checkPlayerCollision(player);

                objectManager.checkObjectTouched(player.getHitbox());
                //Verificare pt level complet(atunci cand jucatorul atinge usa)
                checkLevelCompletion();

                if (player.isDoorTouched() && !showLevelCompleted) {
                    System.out.println("Player has touched the door but level completion UI not shown yet!");
                    showLevelCompleted = true;
                    levelCompleted = true;
                }

                /*if (player.isDead()) {
                    deathTimer = 0;
                }*/
            } else {
                // Player is dead, increment death timer
                deathTimer++;
                player.update();
                if (deathTimer >= DEATH_DELAY) {
                    isGameOver = true;
                }
            }
        } else if (isPaused) {
            pause.update();
        } else if (isGameOver) {
            gameOver.update();
        } else if (showLevelCompleted) {
            completedLevelUI.update();
        }else{
            if(load){
                ArrayList<Integer> values = dataBase.loadGame();
                if (values != null && !values.isEmpty() && values.size() >= 5) {
                    loadGame(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4));
                } else {
                    System.out.println("No saved game data found or error loading game.");
                }
                load = false;
            }
        }
    }

    private void checkLevelCompletion() {
        boolean doorReached = objectManager.playerReachedDoor();

        if (doorReached || levelCompleted) {
            System.out.println("Level completion check: Player reached door (" + doorReached +
                    ") or level completed flag is true (" + levelCompleted + ")");
            showLevelCompleted = true;
            levelCompleted = true; // Ensure both flags are set
        }
    }


    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        // Move camera when player approaches screen edges
        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        // Clamp camera position to level bounds
        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }
    @Override
    public void draw(Graphics g) {
        levelManager.draw(g,xLvlOffset);
        player.render(g,xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        player.drawUI(g, xLvlOffset);

        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pause.draw(g);
        } else if (isGameOver) {
            gameOver.draw(g);
        } else if (showLevelCompleted) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            completedLevelUI.draw(g);
        }

    }
    public void loadNextLevel() throws ResourceNotFoundException {
        // Apelăm metoda din levelManager
        levelManager.loadNextLevel();

        // Resetăm starea jocului pentru noul nivel
        resetAll();

        // IMPORTANT: Setează poziția player-ului explicit pentru noul nivel
        float spawnX = 5 * Game.TILES_SIZE;
        float spawnY = 5 * Game.TILES_SIZE;
        player.setPosition(spawnX, spawnY);

        player.setCurrentLevel(levelManager.getLevelIndex());
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());

        // Resetăm flag-urile de completare nivel
        showLevelCompleted = false;
        levelCompleted = false;

        // Asigură-te că și objectManager este resetat/reîncărcat pentru noul nivel
        objectManager.resetAll();

        // Asigură-te că player cunoaște ușile din noul nivel
        if (objectManager.getDoors() != null && !objectManager.getDoors().isEmpty()) {
            player.setDoors(objectManager.getDoors());
        }

        // Resetează camera offset
        xLvlOffset = 0;

        System.out.println("Nivel nou încărcat: " + levelManager.getLevelIndex());
    }

    public void resetAll() throws ResourceNotFoundException {
        // Reset player position and state
        float spawnX = 5 * Game.TILES_SIZE;
        float spawnY = 5 * Game.TILES_SIZE;

        player = Player.getInstance(spawnX, spawnY, (int)(128 * SCALE), (int)(128 * SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.resetHealth();

        // Reinitialize enemies
        enemyManager = new EnemyManager(this);

        isGameOver = false;
        showLevelCompleted = false;
        levelCompleted = false;
        isPaused = false;
        deathTimer = 0;
        xLvlOffset = 0;

        // Resetează și reinclude ușa
        if (objectManager != null) {
            objectManager.resetAll();
        } else {
            objectManager = new ObjectManager(this);
        }

        // Asigură-te că playerul cunoaște ușile
        if (objectManager.getDoors() != null && !objectManager.getDoors().isEmpty()) {
            player.setDoors(objectManager.getDoors());
        }
    }

        public void render(Graphics g) {
        if (player.isDead()) {
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black overlay
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOver = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(gameOver);
            g.drawString(gameOver, Game.GAME_WIDTH / 2 - textWidth / 2, Game.GAME_HEIGHT / 2);

            // Show restart countdown
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartText = "Restarting in " + ((DEATH_DELAY - deathTimer) / 60 + 1) + " seconds...";
            textWidth = g.getFontMetrics().stringWidth(restartText);
            g.drawString(restartText, Game.GAME_WIDTH / 2 - textWidth / 2, Game.GAME_HEIGHT / 2 + 50);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    //public void mouseDragged(MouseEvent e) {
        //if (paused)
          //  pauseOverlay.mouseDragged(e);
    //}

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if (isPaused)
            pause.mousePressed(e);
        else if (isGameOver)
            gameOver.mousePressed(e);
        else if (showLevelCompleted)
            completedLevelUI.mousePressed(e);

        // Dupa ce a incarcat lvlData, verificam si-l asezam corect daca e in aer


    }

    @Override
    public void mouseReleased(MouseEvent e) throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        if (isPaused)
            pause.mouseReleased(e);
        else if (isGameOver)
            gameOver.mouseReleased(e);
        else if (showLevelCompleted)
            completedLevelUI.mouseReleased(e);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        if (isPaused)
            pause.mouseMoved(e);
        else if (isGameOver)
            gameOver.mouseMoved(e);
        else if (showLevelCompleted)
            completedLevelUI.mouseMoved(e);

    }

    public void unpauseGame() {
        isPaused = false;
    }
    public void setLoadGame(boolean load) {
        this.load = load;
    }

    @Override
    public void keyPressed(KeyEvent e) throws ResourceNotFoundException {
        if (isGameOver) {
            gameOver.keyPressed(e);
            return;
        }
        if (player.isDead()) {
            return; // Ignore input if player is dead
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                player.setUp(true);
                player.setJump(true);
                break;
            case KeyEvent.VK_DOWN:
                player.setDown(true);
                player.setCrouch(true);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setAttacking(true);
                break;
            case KeyEvent.VK_M:
               GameState.state = GameState.MENU;
                break;
            case KeyEvent.VK_ESCAPE:
                isPaused = !isPaused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (player.isDead()) {
            return; // Ignore input if player is dead
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                player.setUp(false);
                player.setJump(false);
                break;
            case KeyEvent.VK_DOWN:
                player.setDown(false);
                player.setCrouch(false);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;
        }
    }
    public static Player getPlayer() {
        return player;
    }

    public void windowsFocusLost()
    {
        player.resetDirBooleans();
    }

    public static LevelManager getLevelManager() {
        return levelManager;
    }

    public void setLevelCompleted(boolean completed) {
        this.levelCompleted = completed;
        System.out.println("Level completed set to: " + completed);
        if (completed) {
            showLevelCompleted = true;
        }
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public static DataBase getDataBase(){return dataBase;}
    // Adaugă aceste metode în clasa Play (după metoda existentă restartCurrentLevel):

    public void restartCurrentLevelFully() throws ResourceNotFoundException {
        // Reset all game state for current level
        float spawnX = 5 * Game.TILES_SIZE;
        float spawnY = 5 * Game.TILES_SIZE;

        // Reset player to spawn position with full health
        player.setPosition(spawnX, spawnY);
        player.resetHealth(); // This should restore full health
        player.resetDirBooleans();

        // Reset score to initial level score (0 or whatever is appropriate)
        score = 0;
        if (player != null) {
            player.setScore(score);
        }

        // Reinitialize enemies for current level
        enemyManager = new EnemyManager(this);

        // Reset game flags
        isGameOver = false;
        showLevelCompleted = false;
        levelCompleted = false;
        deathTimer = 0;

        // Reset camera offset
        xLvlOffset = 0;

        // Reset objects (doors, collectibles, etc.)
        if (objectManager != null) {
            objectManager.resetAll();
        } else {
            objectManager = new ObjectManager(this);
        }

        // Ensure player knows about doors in current level
        if (objectManager.getDoors() != null && !objectManager.getDoors().isEmpty()) {
            player.setDoors(objectManager.getDoors());
        }

        System.out.println("Level restarted fully - Health: " + player.getHealth() + ", Score: " + score);
    }

    public void saveCurrentGameState() {
        try {
            DataBase db = DataBase.getInstance();

            // Get current game state
            int currentLevelIndex = levelManager.getLevelIndex();
            int currentHearts = player.getHealth(); // Assuming player has getHealth() method
            int currentScore = score; // Use the class field score
            int currentXPos = (int) player.getHitbox().x;
            int currentYPos = (int) player.getHitbox().y;

            // Save to database
            db.saveGame(currentLevelIndex, currentHearts, currentScore, currentXPos, currentYPos);

            System.out.println("Game saved successfully!");
            System.out.println("Level: " + currentLevelIndex + ", Hearts: " + currentHearts +
                    ", Score: " + currentScore + ", Pos: (" + currentXPos + "," + currentYPos + ")");

        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
