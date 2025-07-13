package PaooGame.Levels;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static PaooGame.Main.Game.TILES_SIZE;

public class LevelManager {

    private final ArrayList<Level> levels;
    private int levelIndex = 0;
    private Game game;
    private BufferedImage[] levelSprite,levelSprite2,levelSprite3;
    private boolean levelCompleted = false;
    private Level currentLevel;

    public LevelManager(Game game) throws ResourceNotFoundException {
        levels = new ArrayList<>();
        this.game = game;
        importOutsideSprites();
        importOutsideSprites2();
        importOutsideSprites3();
        buildLevels();

    }

    private void buildLevels() {
        // Adăugăm nivelul 1
        Level levelOne = new Level(LoadSave.GetLevelData(0), 0);
        levels.add(levelOne);

        // Adăugăm nivelul 2
        Level levelTwo = new Level(LoadSave.GetLevelData(1), 1);
        levels.add(levelTwo);
        // Adăugăm nivelul 3
        Level levelThree = new Level(LoadSave.GetLevelData(2), 2);
        levels.add(levelThree);

        // Setăm nivelul curent ca fiind primul
        currentLevel = levels.get(levelIndex);
    }
    private void importOutsideSprites() throws ResourceNotFoundException {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j < 4; j++) {
            for(int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }
    private void importOutsideSprites2() throws ResourceNotFoundException {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS_2);
        levelSprite2 = new BufferedImage[180];
        for(int j = 0; j < 9; j++) {
            for(int i = 0; i < 20; i++) {
                int index = j*20 + i;
                levelSprite2[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }
    private void importOutsideSprites3() throws ResourceNotFoundException {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS_3);
        levelSprite3 = new BufferedImage[561];
        for(int j = 0; j < 17; j++) {
            for(int i = 0; i < 33; i++) {
                int index = j*33 + i;
                levelSprite3[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g,int lvlOffset){
        for(int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for(int i = 0; i < currentLevel.getLevelData()[0].length; i++) {
                int index = currentLevel.getSpriteIndex(i, j);
                if (levelIndex == 0) {
                    // Nivel 1 - folosește levelSprite
                    if(index >= 0 && index < levelSprite.length) {
                        g.drawImage(levelSprite[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
                    }
                } else if (levelIndex == 1) {
                    // Nivel 2 - folosește levelSprite2
                    if(index >= 0 && index < levelSprite2.length) {
                        g.drawImage(levelSprite2[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
                    }
                } else if (levelIndex == 2) {
                    // Nivel 3 - folosește levelSprite3 când va fi implementat
                    // Deocamdată folosește levelSprite2 sau levelSprite
                    if(index >= 0 && index < levelSprite3.length) {
                        g.drawImage(levelSprite3[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
                    }
                }
            }
        }
    }

    public void update() {
    }

    public int getLevelIndex(){
        return levelIndex;
    }

    public Level getCurrentLevel() {return currentLevel;}

    public void setLevelCompleted(boolean completed) {
        this.levelCompleted = completed;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    // Metoda pentru a trece la următorul nivel când ușa e folosită
    public void loadNextLevel() {
        levelIndex++;

        if (levelIndex < levels.size()) {
            currentLevel = levels.get(levelIndex);
        } else {
            // If no more levels, we could loop back to the first one or handle differently
            levelIndex = 0;
            currentLevel = levels.get(levelIndex);
        }

        // Actualizează nivelul curent al jucătorului pentru coliziuni
        Play.getPlayer().setCurrentLevel(levelIndex ); // +1 pentru că indexul începe de la 0, dar nivelurile de la 1

        // Actualizează datele nivelului pentru player
        Play.getPlayer().loadLvlData(currentLevel.getLevelData());

        levelCompleted = false;
    }
    public void loadGame(int levelIndex, int hearts, int score, int xPos, int yPos){
        if (levelIndex >= 0 && levelIndex < levels.size()) {
            this.levelIndex = levelIndex;
            currentLevel = levels.get(levelIndex);

            // Update player with loaded data
            Play.getPlayer().loadLvlData(currentLevel.getLevelData());
            Play.getPlayer().setCurrentLevel(levelIndex );
            Play.getPlayer().getHitbox().x = xPos;
            Play.getPlayer().getHitbox().y = yPos;
            Play.getPlayer().setScore(score);
            Play.getPlayer().setHealth(hearts);

            System.out.println("Game loaded successfully! Level: " + levelIndex +
                    ", Hearts: " + hearts +
                    ", Score: " + score +
                    ", Position: (" + xPos + "," + yPos + ")");
        } else {
            System.err.println("Invalid level index for loading: " + levelIndex);
        }

    }
    public Level getLevel(int index) {
        if(index >= 0 && index < levels.size()){
            return levels.get(index);
        }
        return null;
    }

}