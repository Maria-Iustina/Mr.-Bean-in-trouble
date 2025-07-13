package PaooGame.Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;



import static PaooGame.Graphics.Constants.Player.*;
import static PaooGame.Graphics.HelpMethods.*;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.Constants;
import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;
import PaooGame.Objects.Car;
import PaooGame.Objects.Door;

public class Player extends Entity {

    // Instanța unică a Singleton-ului
    private static Player instance;

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;//aici am pus fps=120/nr animatii per secunda=4
    private int playerAction = IDLE;            // NU CRED CA 8 E BUN, NU STIU CE SA PUN
    private boolean left, up, right, down, jump, crouch;
    private float playerSpeed = 1.0f;
    private boolean moving = false, attacking = false;
    private int[][] lvlData;
    private float xDrawOffset = 48 * Game.SCALE;
    private float yDrawOffset = 100 * Game.SCALE;

    private static final int DEATH_ANI_SPEED = 20; // încetinește animația morții
    private boolean doorTouched = false;

    private boolean isDead = false;

    private boolean isInvulnerable = false;
    private int invulnerableTime = 0;
    private final int INVULNERABLE_DURATION = 120;

    //Jumping/Gravity
    private float airSpeed = 0f;
    private float gravity = 0.01f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;


    private List<Door> doors;

    private int currentLevel = 0;

    // lifeStatus UI
    private BufferedImage statusBar;
    private BufferedImage fullHeart;
    private BufferedImage emptyHeart;

    // score
    private int score = 0;
    private boolean scoreAnimationActive = false;
    private int scoreAnimationTime = 0;
    private final int SCORE_ANIMATION_DURATION = 60; // 1 second at 60 FPS
    private int scoreIncrease = 0;

    // Added facing direction
    private boolean facingRight = true;

    private Player(float x, float y, int width, int height) throws ResourceNotFoundException {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));

        this.maxHealth = 5; // 5 hearts, 5 chances
        this.currentHealth = maxHealth; // currentHealth = maxHealth
        loadHealthUI();

    }
    // Metoda statică pentru a obține instanța unică
    public static Player getInstance(float x, float y, int width, int height) throws ResourceNotFoundException {
        if (instance == null) {
            instance = new Player(x, y, width, height);
        }
        return instance;
    }
    public static void resetInstance() {
        instance = null;
    }
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        // Update hitbox position
        hitbox.x = x;
        hitbox.y = y;
    }
    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
        this.invulnerableTime = 0;
    }

    public void updateHealth(int value) {
        currentHealth += value;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    private void loadHealthUI() throws ResourceNotFoundException {
        statusBar = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        fullHeart = LoadSave.GetSpriteAtlas(LoadSave.HEART_FULL);
        emptyHeart = LoadSave.GetSpriteAtlas(LoadSave.HEART_EMPTY);
    }

    public int getHealth(){
        return currentHealth;
    }

    public void resetHealth() {
        currentHealth = maxHealth;
    }

    public void setHealth(int health){
        currentHealth = health;
    }

    private void updateFacingDirection() {
        if (right) {
            facingRight = true;
        } else if (left) {
            facingRight = false;
        }
    }


    private void updateAnimationTick() {
        aniTick++;
        int currentAniSpeed = isDead ? DEATH_ANI_SPEED : aniSpeed;

        if (aniTick >= currentAniSpeed) {
            aniTick = 0;
            aniIndex++;

            // Dacă este mort și a terminat toate cadrele, păstrează ultimul cadru
            if (isDead && aniIndex >= GetSpriteAmount(DEAD)) {
                aniIndex = GetSpriteAmount(DEAD) - 1;
            } else if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    public void render(Graphics g, int lvlOffset) {
        BufferedImage currentFrame = animations[playerAction][aniIndex];
        int x = (int) (hitbox.x - xDrawOffset) - lvlOffset;
        int y = (int) (hitbox.y - yDrawOffset);

        if (isInvulnerable && invulnerableTime % 20 >= 10) {
            return; // Nu desena în acest cadru pentru efect de pâlpâire
        }
        if (facingRight) {
            g.drawImage(currentFrame, x, y, width, height, null);
        } else {
            g.drawImage(currentFrame, x + width, y, -width, height, null);
        }
    }
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }
    // Adaugă această metodă în clasa Player

    private List<Car> cars = new ArrayList<>();

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }


    private void loadAnimations() throws ResourceNotFoundException {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[6][]; // VECTOR CARE CONTINE CELE 6 MISCARI ALE PLAYER-ULUI

        loadActionAnimation(IDLE, "/player/Idle.png", 7, 128, 128);
        loadActionAnimation(RUNNING, "/player/Run.png", 10, 128, 128); // dacă îl adaugi mai târziu
        loadActionAnimation(JUMP, "/player/Jump.png", 10, 128, 128); // presupunem că e Jump
        loadActionAnimation(DEAD, "/player/Dead.png", 5, 128, 128);
        loadActionAnimation(ATTACK, "/player/Attack_1.png", 6, 128, 128);
        loadActionAnimation(CROUCH, "/player/Jump.png", 2, 128, 128);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData,currentLevel))
            inAir = true;

    }

    private void loadActionAnimation(int actionType, String path, int frameCount, int frameW, int frameH) {
        InputStream is = getClass().getResourceAsStream(path);
        BufferedImage img = null;

        try {
            img = ImageIO.read(is);
            if (img == null) {
                throw new RuntimeException("Couldn't load image: " + path);
            }

            System.out.println("Loaded " + path + " with width=" + img.getWidth() + ", height=" + img.getHeight());

            if (img.getWidth() < frameCount * frameW) {
                System.err.println("ERROR: Too many frames for image: " + path);
            }

            BufferedImage[] frames = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = img.getSubimage(i * frameW, 0, frameW, frameH);
            }

            animations[actionType] = frames;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkObjectTouch(float x, float y) {
        if (doors == null) return false;

        Rectangle futureHitbox = new Rectangle((int) x, (int) y, (int) hitbox.width, (int) hitbox.height);

        for (Door door : doors) {
            if (door.getHitbox().intersects(futureHitbox) && door.isBlocking()) {
                return true; // există coliziune cu o ușă închisă
            }
        }


        return false;
    }



    private void updatePos() {
        moving = false;

        if (jump)
            jump();
        //if(!left && !right && !inAir)
        // return;
        if (!inAir)
            if ((!left && !right))
                return;

        float xSpeed = 0;

        float currentSpeed = crouch ? playerSpeed * 0.5f : playerSpeed;

        if (left) {
            xSpeed -= currentSpeed;

        } else if (right) {
            xSpeed += currentSpeed;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData,currentLevel)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData,currentLevel)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);
        moving = (xSpeed != 0 || crouch);

    }

    private void jump() {
        if (inAir || crouch)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData,currentLevel)) {
            hitbox.x += xSpeed;

        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }


    // Update the update method to handle death state
    public void update() {
        if (!isDead) {
            // Gestionează perioada de invulnerabilitate
            if (isInvulnerable) {
                invulnerableTime++;
                if (invulnerableTime >= INVULNERABLE_DURATION) {
                    isInvulnerable = false;
                    invulnerableTime = 0;
                }
            }
            if (scoreAnimationActive) {
                scoreAnimationTime++;
                if (scoreAnimationTime >= SCORE_ANIMATION_DURATION) {
                    scoreAnimationActive = false;
                }
            }

            updatePos();
            updateAnimationTick();
            setAnimation();
            updateFacingDirection();
            checkDoorCollision();
            checkCarCollision();
        } else {
            // Only update animation when dead
            updateAnimationTick();
        }
    }


        private void checkCarCollision() {
            if (cars != null) {
                for (Car car : cars) {
                    if (hitbox.intersects(car.getHitbox())) {

                            System.out.println("Player touched car!");
                            // Level completion will be handled by


                }
            }
        }
    }


    // Update setAnimation to prioritize death animation
    private void setAnimation() {
        int startAni = playerAction;

        if (isDead) {
            playerAction = DEAD;
        } else if (inAir) {
            playerAction = JUMP;
        } else if (crouch) {
            playerAction = CROUCH;
        } else if (attacking) {
            playerAction = ATTACK;
        } else if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (startAni != playerAction)
            resetAniTick();
    }

    // Add method to set death state
    public void setDead(boolean dead) {
        this.isDead = dead;
        if (isDead) {
            resetDirBooleans();
            playerAction = DEAD;
            resetAniTick();
        }
    }

    public boolean isDead() {
        return isDead;
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
        jump = false;
        crouch = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJump(boolean jump) {
        this.jump = jump;

    }
    public void setDoorTouched(boolean doorTouched) {
        this.doorTouched = doorTouched;
        System.out.println("Door touched state set explicitly to: " + doorTouched);
    }

    public boolean isDoorTouched() {
        return doorTouched;
    }
    public boolean isDeathAnimationComplete() {
        return isDead && aniIndex >= GetSpriteAmount(DEAD) - 1;
    }
    private void checkDoorCollision() {
        if (doors != null) {
            for (Door door : doors) {
                // Check for collision with the door
                if (hitbox.intersects(door.getHitbox())) {
                    // Try to open the door if not already open
                    if (!door.isOpen()) {
                        door.setOpen(true);
                        System.out.println("Player touched door and opened it");
                    }

                    // If the door is fully open, mark as touched
                    if (door.isOpen() && !door.isBlocking()) {
                        doorTouched = true;
                        System.out.println("Player touching open door! Door is fully open. doorTouched=" + doorTouched);
                    }
                }
            }
        }
    }


    public void setCrouch(boolean crouch) {
        this.crouch = crouch;
    }
    public int getScore() {
        return score;
    }

    public void drawUI(Graphics g, int lvlOffset) {
        // Status bar position
        int statusBarX = (int) (60 * Game.SCALE);
        int statusBarY = (int) (40 * Game.SCALE);
        int statusBarWidth = (int) (270 * Game.SCALE);
        int statusBarHeight = (int) (150 * Game.SCALE);

        // Hearts position
        int heartsXStart = (int) (110 * Game.SCALE);
        int heartsYStart = (int) (50 * Game.SCALE);
        int heartWidth = (int) (35 * Game.SCALE);
        int heartHeight = (int) (32 * Game.SCALE);
        int heartSpacing = (int) (40 * Game.SCALE);

        // Draw status bar background
        g.drawImage(statusBar, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Draw hearts based on current health
        for (int i = 0; i < maxHealth; i++) {
            if (i < currentHealth) {
                // Draw full heart
                g.drawImage(fullHeart, heartsXStart + i * heartSpacing, heartsYStart, heartWidth, heartHeight, null);
            } else {
                // Draw empty heart
                g.drawImage(emptyHeart, heartsXStart + i * heartSpacing, heartsYStart, heartWidth, heartHeight, null);
            }
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("SERIF", Font.PLAIN, (int)(40 * Game.SCALE)));
        g.drawString("Score: " + score, (int)(100 * Game.SCALE), (int)(170 * Game.SCALE));
    }

    public void setScore(int score) {
        int oldScore = this.score;
        this.score = score;

        // Show score animation if score increased
        if (score > oldScore) {
            scoreIncrease = score - oldScore;
            scoreAnimationActive = true;
            scoreAnimationTime = 0;
        }
    }
}


