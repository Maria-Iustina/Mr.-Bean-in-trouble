package PaooGame.Entities;

import PaooGame.Main.Game;

import static PaooGame.Graphics.Constants.EnemyConstants.*;

public class Spike extends Enemy {

    public Spike(float x, float y) {
        super(x, y, SPIKE_WIDTH, SPIKE_HEIGHT, SPIKE);
        initHitbox(x, y, (int) (99 * Game.SCALE), (int) (70 * Game.SCALE));
        this.startX = x;
        // Marcăm inamicul Spike ca fiind static
        this.isStatic = true;
    }
}