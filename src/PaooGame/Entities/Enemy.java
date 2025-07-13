package PaooGame.Entities;

import PaooGame.Main.Game;

import java.awt.geom.Rectangle2D;

import static PaooGame.Graphics.Constants.EnemyConstants.GetSpriteAmount;

public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;
    protected boolean isStatic = false;

    protected float speed = 0.2f;
    protected boolean movingLeft = true;
    protected float patrolDistance = 64 * 4;  // 8 tile-uri in total (4 stânga, 4 dreapta)
    protected float startX;

    protected int attacking = 0;

    protected int attackBoxOffsetX;
    protected boolean attackChecked;

    /*protected float startX; // Poziția de start pentru patrulare
    protected int walkDir = LEFT; // Direcția de mers
    protected float walkSpeed; // Viteza curentă
    protected boolean movingL = true; // Dacă se mișcă spre stânga*/

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        this.startX = x;
    }

    protected void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (65 * Game.SCALE), (int) (28 * Game.SCALE));
        attackBoxOffsetX = (int) (10 * Game.SCALE);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }

    private void move() {
        // Verificăm dacă inamicul este static și nu facem nimic în acest caz
        if (isStatic)
            return;

        if (movingLeft) {
            hitbox.x -= speed;
            if (hitbox.x <= startX - patrolDistance)
                movingLeft = false;
        } else {
            hitbox.x += speed;
            if (hitbox.x >= startX + patrolDistance)
                movingLeft = true;
        }
    }

    public void update() {
        updateAnimationTick();
        move();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(float x, float y) {
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.startX = x;
    }
}