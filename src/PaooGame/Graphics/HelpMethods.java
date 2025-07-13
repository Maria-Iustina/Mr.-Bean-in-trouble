package PaooGame.Graphics;

import PaooGame.Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData,int currentLevel) {
        if (!IsSolid(x, y, lvlData,currentLevel))
            if (!IsSolid(x + width, y + height, lvlData,currentLevel))
                if (!IsSolid(x + width, y, lvlData,currentLevel))
                    if (!IsSolid(x, y + height, lvlData,currentLevel))
                        return true;
        return false;
    }

    public static boolean IsSolid(float x, float y, int[][] lvlData,int currentLevel) {
        int maxWidth = (lvlData[0].length - 2) * Game.TILES_SIZE;
        // Verifică dacă este în afara limitelor jocului
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        // Verifică dacă indicii sunt în limitele matricei
        if (yIndex >= lvlData.length || xIndex >= lvlData[0].length)
            return true;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (currentLevel == 0) {
            return value == 28 || value == 39 || value == 29 || value == 27 || value == 26 || value == 36 || value >= 37;
        } else if (currentLevel == 1) {
            return value == 21 || value == 22 || value == 23 || value == 61 || value == 62 || value == 63  || value == 121  || value == 123 || value == 122  || value == 141  || value == 142 || value == 143 || value == 1;// value >= 21;
        }else if (currentLevel == 2) {
            return (value >=473 && value <=477) || ( value >=504 && value <= 510) || (value >= 537 && value<=538) || (value >= 541 && value<=543)||(value >= 545 && value<=548)|| value == 553 || value == 573 ;// value >= 21;
        }

        return value==12;//val implicita in caz ca nu e level
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed)
    {
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
        if(xSpeed > 0){
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        }else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed)
    {
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if(airSpeed > 0){
            // Falling-touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        }else {
            // Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData,int currentLevel) {
        // Check if there's a solid block right below the player's feet
        // Increase check distance to ensure we detect ground properly
        float checkDistance = 1f;

        // Check multiple points along the bottom of the hitbox
        int pixelCheck = 4;
        float xStep = hitbox.width / pixelCheck;

        for (int i = 0; i <= pixelCheck; i++) {
            float xToCheck = hitbox.x + i * xStep;

            if (IsSolid(xToCheck, hitbox.y + hitbox.height + checkDistance, lvlData,currentLevel)) {
                return true;
            }
        }
        return false;
    }
}