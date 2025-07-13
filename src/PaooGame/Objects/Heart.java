package PaooGame.Objects;

import PaooGame.Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Heart extends GameObject {

    private float hoverOffset;
    private final int maxHoverOffset;
    private int hoverDirection = 1;


    public Heart(int x, int y, int objectType) {
        super(x, y, objectType);
        initCollisionBox(32, 32);

        // Use Game.SCALE for consistency with the rest of your game
        maxHoverOffset = (int)(5 * Game.SCALE);
        System.out.println("Heart created at position: " + x + ", " + y);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }


    private void updateHover() {
        // Make the heart float up and down for visual effect
        hoverOffset += (0.1f * Game.SCALE * hoverDirection);

        if(hoverOffset >= maxHoverOffset)
            hoverDirection = -1;
        else if(hoverOffset < 0)
            hoverDirection = 1;

        collisionBox.y = y + hoverOffset;
    }

    @Override
    protected void initCollisionBox(int width, int height) {
        collisionBox = new Rectangle2D.Float(x, y, width, height);
        // Adding offset values for visual positioning
        xOffset = 0;
        yOffset = 0;
    }
    public void drawCollisionBox(Graphics g, int xLevelOffset) {
        // Draw the hitbox with a semi-transparent red color for debugging
        g.setColor(new Color(255, 0, 0, 128));
        g.fillRect(
                (int)(collisionBox.x - xLevelOffset),
                (int)(collisionBox.y),
                (int)collisionBox.width,
                (int)collisionBox.height
        );
    }
}