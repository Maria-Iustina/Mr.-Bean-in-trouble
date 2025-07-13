package PaooGame.Graphics;

import PaooGame.Main.Game;

import java.util.ArrayList;

public class Constants {
    public static final int ANIMATION_SPEED_OBJECTS = 8;

    public static class GameCONST {
        public final static int UPS = 200; // updates/second
        public final static int FPS = 120; // frames/second
        public final static int TILE_DEFAULT_SIZE = 32;
        public final static float SCALE = 1f;
        public final static int WIDTH_TILES = 50;
        public final static int HEIGHT_TILES = 25;
    }


    public static class EnemyConstants {
        // Define all enemy types first
        public static final int BEE = 0;
        public static final int GRANDMA = 1;
        public static final int CAT = 2;
        public static final int BOY = 3;
        public static final int SPIKE = 4;

        // Define animation states
        public static final int RUNNING = 0;

        // Define dimensions for Bee
        public static final int BEE_WIDTH_DEFAULT = 99;
        public static final int BEE_HEIGHT_DEFAULT = 77;
        public static final int BEE_WIDTH = 60;
        public static final int BEE_HEIGHT = 60;

        // Define dimensions for Spike
        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = 60;
        public static final int SPIKE_HEIGHT = 60;

        // Define dimensions for Grandma
        public static final int GRANDMA_WIDTH_DEFAULT = 166;
        public static final int GRANDMA_HEIGHT_DEFAULT = 170;
        public static final int GRANDMA_WIDTH = 70;
        public static final int GRANDMA_HEIGHT = 70;

        // Define dimensions for Cat
        public static final int CAT_WIDTH_DEFAULT = 111;
        public static final int CAT_HEIGHT_DEFAULT = 117;
        public static final int CAT_WIDTH = 70;
        public static final int CAT_HEIGHT = 70;

        public static final int BOY_WIDTH_DEFAULT = 80;
        public static final int BOY_HEIGHT_DEFAULT = 148;
        public static final int BOY_WIDTH = 70;
        public static final int BOY_HEIGHT = 70;

        // Then define your sprite amount method
        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case BEE:
                    return 3;
                case GRANDMA:
                    return 3;
                case CAT:
                    return 2;
                case BOY:
                    return 5;
                default:
                    return 1;
            }
        }
    }

    public static class UI {
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 32;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
        }
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 144;
            public static final int B_HEIGHT_DEFAULT = 72;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

    }
    public  static class Directions
    {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class Player {

        public static class Images {
            public static final String player_idle = "/player/Idle.png";
            public static final String player_run = "/player/Run.png";
            public static final String player_jump = "/player/Jump.png";
            public static final String player_dead = "/player/Dead.png";
            public static final String player_attack1 = "/player/Attack_1.png";
        }

        public static final int IDLE = 0;               // stat pe loc
        public static final int RUNNING = 1;            // jucatorul alearga
        public static final int JUMP = 2;               // saritura
        public static final int DEAD = 3;               // // jucatorul moare
        public static final int ATTACK = 4;
        public static final int CROUCH = 5;


        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case IDLE:
                    return 7;   // aici nu sunt 7 animatii in poza cu idle?
                case RUNNING:       // DIN CE AM INTELES EU AR TREBUI SA FIE = CU NR DE ANIMATII DIN POZA RESPECTIVA
                    return 10;
                case JUMP:
                    return 10;
                case DEAD:
                    return 5;
                case ATTACK:
                    return 6;
                case CROUCH:
                    return 2;
                default:
                    return 0;
            }
        }
    }
}
