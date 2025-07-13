package PaooGame.Entities;

import PaooGame.Main.Game;

import static PaooGame.Graphics.Constants.EnemyConstants.*;

public class Grandma extends Enemy {

        public Grandma(float x, float y) {
            super(x, y, GRANDMA_WIDTH, GRANDMA_HEIGHT, GRANDMA);
            initHitbox( x, y, (int) (99 * Game.SCALE), (int) (70 * Game.SCALE));
            // Setăm viteza specifică pentru bunici
            this.speed = 0.15f;
            // Păstrăm poziția de start pentru patrulare
            this.startX = x;
            // Distanța de patrulare mai mică pentru bunici
            this.patrolDistance = 64 * 2; // 4 tile-uri (2 stânga, 2 dreapta)
        }

    @Override
    public void update() {
        super.update();
        // alte lucruri specifice bunicii (dacă există)
    }
    }
