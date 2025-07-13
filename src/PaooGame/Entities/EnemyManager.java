package PaooGame.Entities;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static PaooGame.Graphics.Constants.EnemyConstants.*;
import static PaooGame.Main.Game.TILES_SIZE;


public class EnemyManager {

    private Play playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public EnemyManager(Play playing) throws ResourceNotFoundException {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    public void addEnemies() {
        enemies.clear();
        int levelIndex = playing.getLevelManager().getCurrentLevel().getLevelIndex();

        // Exemplu de adăugare inamici pe nivel
        if (levelIndex == 0) {
            enemies.add(EnemyFactory.createEnemy(BEE, 30*TILES_SIZE, 16*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(GRANDMA, 88*TILES_SIZE, 18*TILES_SIZE));
        } else if (levelIndex == 1) {
            enemies.add(EnemyFactory.createEnemy(CAT, 30*TILES_SIZE, 20*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(CAT, 80*TILES_SIZE, 17*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(SPIKE, 10*TILES_SIZE, 16*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(SPIKE, 8*TILES_SIZE, 16*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(SPIKE, 51*TILES_SIZE, 18*TILES_SIZE));

        } else if (levelIndex == 2) {
            enemies.add(EnemyFactory.createEnemy(BOY, 30*TILES_SIZE, 21*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(BOY, 10*TILES_SIZE, 21*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(BOY, 50*TILES_SIZE, 21*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(BOY, 70*TILES_SIZE, 21*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(BOY, 90*TILES_SIZE, 21*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(SPIKE, 63*TILES_SIZE, 16*TILES_SIZE));
            enemies.add(EnemyFactory.createEnemy(SPIKE, 62*TILES_SIZE, 16*TILES_SIZE));
        }
    }

    public void update() {
        for (Enemy e : enemies) {
            e.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (Enemy e : enemies) {
            BufferedImage[][] spriteArr = getEnemySpriteArray(e);
            if (spriteArr == null) continue;

            int x = (int) e.getHitbox().x - xLvlOffset;
            int y = (int) e.getHitbox().y;
            int width = e.getWidth();
            int height = e.getHeight();

            BufferedImage image = spriteArr[e.getEnemyState()][e.getAniIndex()];
            Graphics2D g2 = (Graphics2D) g;

            // Condiție specială pentru pisică - inversăm logica
            if (e instanceof Cat) {
                if (e.isMovingLeft()) {
                    g2.drawImage(image, x + width, y, -width, height, null);
                } else {
                    g2.drawImage(image, x, y, width, height, null);
                }
            }
            // Condiție specială pentru câine - corectăm orientarea
            else if (e instanceof Boy) {
                if (e.isMovingLeft()) {
                    g2.drawImage(image, x + width, y, -width, height, null);
                } else {
                    g2.drawImage(image, x, y, width, height, null);
                }
            }
            else {
                // Pentru ceilalți inamici, păstrăm logica actuală
                if (e.isMovingLeft()) {
                    g2.drawImage(image, x, y, width, height, null);
                } else {
                    g2.drawImage(image, x + width, y, -width, height, null);
                }
            }
        }
    }

    private BufferedImage[][] getEnemySpriteArray(Enemy e) {
        if (e instanceof Bee) return beeArr;
        if (e instanceof Grandma) return grandmaArr;
        if (e instanceof Cat) return catArr;
        if (e instanceof Boy) return boyArr;
        if (e instanceof Spike) return spikeArr;
        return null;
    }

    public void checkPlayerCollision(Player player) {
        if (player.isDead() || player.isInvulnerable()) return;

        for (Enemy e : enemies) {
            if (e.getHitbox().intersects(player.getHitbox())) {
                player.updateHealth(-1);
                if (player.getHealth() <= 0) {
                    player.setDead(true);
                } else {
                    player.setInvulnerable(true);
                }
                return;
            }
        }
    }

    private void loadEnemyImgs() throws ResourceNotFoundException {
        loadBeeImgs();
        loadGrandmaImgs();
        loadCatImgs();
        loadBoyImgs();
        loadSpikeImgs();
    }

    private void loadBeeImgs() throws ResourceNotFoundException {
        beeArr = new BufferedImage[1][3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BEE_SPRITE);
        for (int i = 0; i < 3; i++)
            beeArr[0][i] = temp.getSubimage(i * BEE_WIDTH_DEFAULT, 0, BEE_WIDTH_DEFAULT, BEE_HEIGHT_DEFAULT);
    }
    private void loadSpikeImgs() throws ResourceNotFoundException {
        spikeArr = new BufferedImage[1][3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SPIKE_SPRITE);
        spikeArr[0][0] = temp.getSubimage(0 * SPIKE_WIDTH_DEFAULT, 0, SPIKE_WIDTH_DEFAULT, SPIKE_HEIGHT_DEFAULT);
    }

    private void loadGrandmaImgs() throws ResourceNotFoundException {
        grandmaArr = new BufferedImage[1][3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GRANDMA_SPRITE);
        for (int i = 0; i < 3; i++)
            grandmaArr[0][i] = temp.getSubimage(i * GRANDMA_WIDTH_DEFAULT, 0, GRANDMA_WIDTH_DEFAULT, GRANDMA_HEIGHT_DEFAULT);
    }

    private void loadCatImgs() throws ResourceNotFoundException {
        catArr = new BufferedImage[1][2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CAT_SPRITE);
        for (int i = 0; i < 2; i++)
            catArr[0][i] = temp.getSubimage(i * CAT_WIDTH_DEFAULT, 0, CAT_WIDTH_DEFAULT, CAT_HEIGHT_DEFAULT);
    }

    private void loadBoyImgs() throws ResourceNotFoundException {
        boyArr = new BufferedImage[1][5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOY_SPRITE);
        for (int i = 0; i < 5; i++)
            boyArr[0][i] = temp.getSubimage(i * BOY_WIDTH_DEFAULT, 0, BOY_WIDTH_DEFAULT,BOY_HEIGHT_DEFAULT);
    }

    // Arrays de animații
    private BufferedImage[][] beeArr, grandmaArr, catArr, boyArr,spikeArr;
}
