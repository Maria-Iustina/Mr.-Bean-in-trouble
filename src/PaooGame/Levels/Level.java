package PaooGame.Levels;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Level {

        private int[][] lvlData;
        private boolean doorAdded = false;
        private int levelScore;
        private int levelIndex;
        private ArrayList<Point2D.Float> coordinates;

        public Level(int[][] lvlData){
            this.lvlData = lvlData;
            this.coordinates = new ArrayList<>();
            this.levelIndex = 0;

        }
    public Level(int[][] lvlData, int levelIndex){
        this.lvlData = lvlData;
        this.coordinates = new ArrayList<>();
        this.levelIndex = levelIndex;
    }


    public ArrayList<Point2D.Float> getCoordinates(int tileValue) {
        ArrayList<Point2D.Float> result = new ArrayList<>();
        for (int y = 0; y < lvlData.length; y++) {
            for (int x = 0; x < lvlData[y].length; x++) {
                if (lvlData[y][x] == tileValue) {
                    result.add(new Point2D.Float(x, y));
                }
            }
        }
        return result;
    }


    public int getSpriteIndex(int x, int y){
        if(y >= 0 && y < lvlData.length && x >= 0 && x < lvlData[y].length) {
            return lvlData[y][x];
        }
        return 0; // Returnează tile-ul default dacă iese din limite
    }

    public int[][] getLevelData(){
        return lvlData;
    }

    public int getLevelScore(){
        return levelScore;
    }
    public int getLevelIndex() {
        return levelIndex;
    }
}
