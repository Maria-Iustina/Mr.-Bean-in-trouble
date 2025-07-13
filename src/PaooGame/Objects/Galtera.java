package PaooGame.Objects;

import PaooGame.Main.Game;

public class Galtera extends GameObject {

    private float hoverOffset;
    private final int maxHoverOffset;
    private int hoverDirection = 1;

    public Galtera(int x, int y) {
        super(x, y, 711);
        initCollisionBox(24, 24);
        xOffset = (int) (8 * Game.SCALE);
        yOffset = (int)(10 * Game.SCALE);

        maxHoverOffset = (int)(10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.06f * Game.SCALE * hoverDirection);
        if(hoverOffset >= maxHoverOffset)
            hoverDirection = -1;
        else if(hoverOffset < 0)
            hoverDirection = 1;
        collisionBox.y = y + hoverOffset;
    }
}