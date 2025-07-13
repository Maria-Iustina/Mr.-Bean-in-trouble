
package PaooGame.Entities;

import PaooGame.Main.Game;

import static PaooGame.Graphics.Constants.EnemyConstants.*;

public class Boy extends Enemy implements EnemyPrototype {

    public Boy(float x, float y) {
        super(x, y, BOY_WIDTH, BOY_HEIGHT, BOY);
        this.speed = 0.5f;
        initHitbox(x, y, (int) (99 * Game.SCALE), (int) (70 * Game.SCALE));
        this.startX = x;
    }

    @Override
    public EnemyPrototype cloneEnemy() {
        return new Boy(this.hitbox.x, this.hitbox.y);
    }

    @Override
    public EnemyPrototype cloneEnemy(float x, float y) {
        return new Boy(x, y);
    }
}