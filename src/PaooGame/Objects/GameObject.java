package PaooGame.Objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {
    protected int x, y;
    protected int objectType;
    protected boolean active = true;
    protected Rectangle2D.Float collisionBox;
    protected float xOffset, yOffset;

    protected int animationTick, animationIndex, animationSpeed = 25;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 17) { // Assuming 17 frames max for animations
                animationIndex = 0;
            }
        }
    }

    protected void initCollisionBox(int width, int height) {
        collisionBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void drawCollisionBox(Graphics g, int xLevelOffset) {
        // For debugging
        g.setColor(Color.RED);
        g.drawRect((int) (collisionBox.x - xLevelOffset), (int) collisionBox.y, (int) collisionBox.width, (int) collisionBox.height);
    }

    public void reset() {
        active = true;
        animationIndex = 0;
        animationTick = 0;
    }

    public Rectangle2D.Float getCollisionBox() {
        return collisionBox;
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getObjectType() {
        return objectType;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

}