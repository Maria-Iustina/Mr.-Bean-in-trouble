package PaooGame.Objects;

import PaooGame.Entities.EnemyManager;
import PaooGame.GameStates.Play;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends GameObject{

    private boolean open = false;
    private boolean fullyOpen = false;
    private boolean playerInteracted = false;

    public Door(int x, int y, int objectType)
    {
        super(x, y, objectType);
        initCollisionBox(65, 61);
        //hitbox pt detectia coliziunii cu usa
        hitbox = new Rectangle(x, y, (int)(65 * PaooGame.Main.Game.SCALE), (int)(61 * PaooGame.Main.Game.SCALE));
    }

    public boolean isBlocking() {
        return !fullyOpen;
    }

    private Rectangle hitbox;

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void update(int lvlIndex, EnemyManager enemyManager) {
        updateAnimationTick();

        // Check if player is touching the door
        if (Play.getPlayer() != null) {
            if (Play.getPlayer().getHitbox().intersects(hitbox)) {
                // Open the door when player touches it
                open = true;

                // If door is fully open and player touches it, mark level as completed
                if (fullyOpen && !playerInteracted) {
                    System.out.println("Player touched fully open door at level: " + lvlIndex);
                    Play.getPlayer().setDoorTouched(true);
                    playerInteracted = true;

                    // Directly notify level completion
                    if (Play.getLevelManager() != null) {
                        Play.getLevelManager().setLevelCompleted(true);
                    }
                }
            }
        }
    }

    @Override
    protected void updateAnimationTick() {
        if (open) {
            if (animationIndex < 4) {
                animationTick++;
                if (animationTick >= 15) {  // Made animation faster
                    animationTick = 0;
                    animationIndex++;
                    if (animationIndex >= 4) {
                        fullyOpen = true;

                        // Ensure player interaction is checked when door becomes fully open
                        if (Play.getPlayer() != null &&
                                Play.getPlayer().getHitbox().intersects(hitbox) &&
                                !playerInteracted) {
                            System.out.println("Door fully opened with player contact!");
                            Play.getPlayer().setDoorTouched(true);
                            playerInteracted = true;

                            // Directly notify level completion
                            if (Play.getLevelManager() != null) {
                                Play.getLevelManager().setLevelCompleted(true);
                            }
                        }
                    }
                }
            }
        } else {
            animationIndex = 0;  // Reset animation when door is closed
            fullyOpen = false;
        }
    }

    public void setOpen(boolean open){
        this.open = open;
        if (!open) {
            fullyOpen = false;
            playerInteracted = false;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isFullyOpen() {
        return fullyOpen;
    }

    @Override
    public void reset() {
        super.reset();
        open = false;
        fullyOpen = false;
        playerInteracted = false;
        animationIndex = 0;
        animationTick = 0;
        active = true;
    }
}