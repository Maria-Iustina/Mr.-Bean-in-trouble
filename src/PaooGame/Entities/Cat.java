package PaooGame.Entities;

import PaooGame.Main.Game;

import static PaooGame.Graphics.Constants.EnemyConstants.*;

public class Cat extends Enemy implements EnemyPrototype {

    public Cat(float x, float y) {
        super(x, y, CAT_WIDTH, CAT_HEIGHT, CAT);
        this.speed = 0.6f;
        initHitbox(x, y, (int) (99 * Game.SCALE), (int) (70 * Game.SCALE));
        this.startX = x;
    }

    @Override
    public EnemyPrototype cloneEnemy() {
        return new Cat(this.hitbox.x, this.hitbox.y);
    }

    @Override
    public EnemyPrototype cloneEnemy(float x, float y) {
        return new Cat(x, y);
    }
}