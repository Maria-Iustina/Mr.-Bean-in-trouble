package PaooGame.Entities;

import PaooGame.Main.Game;

import static PaooGame.Graphics.Constants.EnemyConstants.*;

public class Bee extends Enemy {

    public Bee(float x, float y) {
        super(x, y, BEE_WIDTH, BEE_HEIGHT, BEE);
        initHitbox( x, y, (int) (99 * Game.SCALE), (int) (70 * Game.SCALE));
        this.startX = x;
    }
}