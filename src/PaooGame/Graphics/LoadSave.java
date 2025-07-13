package PaooGame.Graphics;

import PaooGame.Main.Game;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import PaooGame.Exceptions.ResourceNotFoundException;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player/Idle.png";
    public static final String LEVEL_ATLAS = "Levels/outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "Levels/level_one_data.txt";
    public static final String LEVEL_ATLAS_2= "Levels/outside_sprites_2.png";
    public static final String LEVEL_TWO_DATA = "Levels/level_two_data.txt";
    public static final String LEVEL_ATLAS_3= "Levels/outside_sprites_3.png";
    public static final String LEVEL_THREE_DATA = "Levels/level_three_data.txt";
    public static final String MENU_BUTTONS = "UI/Menu/MenuButtons.png";
    public static final String MENU_BACKGROUND = "UI/Menu/menu.png";
    public static final String SOUND_BUTTONS = "UI/Menu/soundButton.png";
    public static final String MENU_BACKGROUND_IMG = "UI/Menu/menuBackground.png";
    public static final String PAUSE_BACKGROUND= "UI/Menu/pause.png";
    public static final String MUSIC_BUTTONS = "UI/Menu/musicButton.png";
    public static final String OTHER_BUTTONS = "UI/Menu/otherButtons.png";
    public static final String DOOR = "Levels/door.png";
    public static final String CAR= "Levels/car.png";

    public static final String BEE_SPRITE = "Enemies/Bee/bee_sprite.png";
    public static final String GRANDMA_SPRITE = "Enemies/Grandma/bunica_sprite.png";
    public static final String BOY_SPRITE = "Enemies/Boy/run.png";
    public static final String CAT_SPRITE = "Enemies/Cat/Pisica.png";
    public static final String SPIKE_SPRITE = "Enemies/metal_spike.png";

    public static final String GAME_OVER_IMAGE = "UI/Menu/gameOver.png";
    public static final String COMPLETED_LEVEL_IMAGE = "UI/Menu/CompletedLevel.png";

    public static final String STATUS_BAR = "status/bar.png";
    public static final String HEART_FULL = "status/heartFull.png";
    public static final String HEART_EMPTY = "status/heartEmpty.png";

    public static final String CLOCK = "Objects/ceas.png";
    public static final String ROSE = "Objects/rose.png";
    public static final String GALTERA = "Objects/galtera.png";


    public static BufferedImage GetSpriteAtlas(String fileName) throws ResourceNotFoundException {
        BufferedImage img;

        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (is == null) {
                throw new ResourceNotFoundException("Nu s-a găsit fișierul: /" + fileName);
            }

            img = ImageIO.read(is);
            if (img == null) {
                throw new ResourceNotFoundException("Nu s-a putut citi imaginea: /" + fileName);
            }
        } catch (IOException e) {
            throw new ResourceNotFoundException("Eroare la citirea imaginii: /" + fileName + " | " + e.getMessage());
        }

        return img;
    }


    public static int[][] GetLevelData(int levelIndex) {
        String levelFile;

        // Selectează fișierul de date corespunzător nivelului
        switch (levelIndex) {
            case 0:
                levelFile = LEVEL_ONE_DATA;
                break;
            case 1:
                levelFile = LEVEL_TWO_DATA;
                break;
            case 2:
                levelFile = LEVEL_THREE_DATA;
                break;
            default:
                levelFile = LEVEL_ONE_DATA; // Implicit nivel 1
        }

        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH * 2]; // Make level twice as wide

        try (InputStream is = LoadSave.class.getResourceAsStream("/" + levelFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < Game.TILES_IN_HEIGHT) {
                String[] numbers = line.split(",");
                for (int col = 0; col < lvlData[row].length; col++) {
                    lvlData[row][col] = Integer.parseInt(numbers[col % numbers.length]);
                }
                row++;
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            for (int i = 0; i < lvlData.length; i++) {
                for (int j = 0; j < lvlData[i].length; j++) {
                    lvlData[i][j] = (j % 10 == 0 || i == lvlData.length - 1) ? 28 : 0; // Simple platform pattern
                }
            }
        }
        return lvlData;
    }
    public static int[][] GetLevelData() {
        return GetLevelData(0); // Încarcă primul nivel implicit
    }

}
