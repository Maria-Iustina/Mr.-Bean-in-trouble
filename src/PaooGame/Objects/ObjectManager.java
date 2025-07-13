package PaooGame.Objects;

import PaooGame.Exceptions.ResourceNotFoundException;
import PaooGame.GameStates.Play;
import PaooGame.Graphics.DataBase;
import PaooGame.Graphics.LoadSave;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class ObjectManager {
    private final Play play;
    private List<BufferedImage> doorImages;

    private BufferedImage heartImage;
    private BufferedImage carImage;
    private BufferedImage clockImage;
    private BufferedImage roseImage;
    private BufferedImage galteraImage;

    private final List<Door> doors;
    private final List<List<Heart>> hearts;

    private final List<List<Clock>> clocks;
    private final List<Car> cars;
    private final List<List<Rose>> roses;
    private final List<List<Galtera>> galtere;

    public ObjectManager(Play play) throws ResourceNotFoundException {
        this.play = play;
        loadImages();

        clocks = new ArrayList<>();
        doors = new ArrayList<>();
        hearts = new ArrayList<>();
        roses = new ArrayList<>();
        galtere = new ArrayList<>();

        cars = new ArrayList<>();

        initHeart(0);

        initDoor(0);
        initDoor(1); // Inițializează ușa pentru nivelul 1
        initCar(2); // Inițializează mașina pentru nivelul 2 (nivel 3 în UI)

        initClocks(0);
        initRoses(1);
        initGaltera(2);
    }

    private void initClocks(int lvlIndex) {
        List<Clock> clocksLevel = new ArrayList<>();

        // Verificăm dacă avem date despre nivel
        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {
            // Similar cu inimile, vom căuta coordonatele specifice pentru ceasuri
            // Presupunem că folosim valoarea 709 pentru ceasuri în nivel
            ArrayList<Point2D.Float> clockCoordinates = play.getLevelManager().getLevel(lvlIndex).getCoordinates(709);

            // Dacă nu există coordonate specifice, putem adăuga manual câteva poziții pentru nivel
            if (clockCoordinates == null || clockCoordinates.isEmpty()) {
                clockCoordinates = new ArrayList<>();

                // Adăugăm manual pozițiile ceasurilor pentru nivelul curent
                if (lvlIndex == 0) {
                    // Poziții pentru nivelul 0 (coordonate în spațiul de tile-uri)
                    clockCoordinates.add(new Point2D.Float(16, 9));
                    clockCoordinates.add(new Point2D.Float(28, 6));
                    clockCoordinates.add(new Point2D.Float(48, 11));
                    clockCoordinates.add(new Point2D.Float(67, 11));
                    clockCoordinates.add(new Point2D.Float(81, 6));
                }
                // Poți adăuga mai multe poziții pentru alte niveluri după necesitate
            }

            // Creăm ceasuri la pozițiile specificate
            for (Point2D.Float clockCoordinate : clockCoordinates) {
                // Convertim coordonatele de tile în coordonate de pixel
                int tileSize = 32;
                int clockX = (int) (clockCoordinate.getX() * tileSize);
                int clockY = (int) (clockCoordinate.getY() * tileSize);

                // Verificăm să nu existe deja un ceas la aceste coordonate
                boolean duplicateFound = false;
                for (Clock existingClock : clocksLevel) {
                    if ((int) existingClock.getCollisionBox().x == clockX &&
                            (int) existingClock.getCollisionBox().y == clockY) {
                        duplicateFound = true;
                        break;
                    }
                }

                // Dacă nu e duplicat, adăugăm noul ceas
                if (!duplicateFound) {
                    Clock newClock = new Clock(clockX, clockY);
                    clocksLevel.add(newClock);

                }
            }
        } else {
            System.out.println("AVERTISMENT: Date nivel negăsite pentru inițializarea ceasurilor");
        }

        clocks.add(clocksLevel);
    }

    private void initGaltera(int lvlIndex) {
        List<Galtera> galtereLevel = new ArrayList<>();

        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {
            ArrayList<Point2D.Float> galteraCoordinates = play.getLevelManager().getLevel(lvlIndex).getCoordinates(710);

            if (galteraCoordinates == null || galteraCoordinates.isEmpty()) {
                galteraCoordinates = new ArrayList<>();

                if (lvlIndex == 2) {
                    galteraCoordinates.add(new Point2D.Float(18, 12));
                    galteraCoordinates.add(new Point2D.Float(26, 7));
                    galteraCoordinates.add(new Point2D.Float(60, 7));
                    galteraCoordinates.add(new Point2D.Float(87, 10));
                }
            }

            for (Point2D.Float galteraCoordinate : galteraCoordinates) {
                int tileSize = 32;
                int galteraX = (int) (galteraCoordinate.getX() * tileSize);
                int galteraY = (int) (galteraCoordinate.getY() * tileSize);

                Galtera newGaltera = new Galtera(galteraX, galteraY);
                galtereLevel.add(newGaltera);

            }
        } else {
            System.out.println("AVERTISMENT: Date nivel negăsite pentru inițializarea galterii");
        }

        // Asigurăm-ne că adăugăm galterea la nivelul corect
        while (galtere.size() <= lvlIndex) {
            galtere.add(new ArrayList<>());
        }
        galtere.set(lvlIndex, galtereLevel);
    }

    private void initRoses(int lvlIndex) {
        List<Rose> rosesLevel = new ArrayList<>();

        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {
            // Schimbăm valoarea de căutare la 710 pentru trandafiri ca să nu se confunde cu ceasurile (709)
            ArrayList<Point2D.Float> roseCoordinates = play.getLevelManager().getLevel(lvlIndex).getCoordinates(710);

            if (roseCoordinates == null || roseCoordinates.isEmpty()) {
                roseCoordinates = new ArrayList<>();

                if (lvlIndex == 1) {
                    roseCoordinates.add(new Point2D.Float(28, 13));
                    roseCoordinates.add(new Point2D.Float(51, 4));
                    roseCoordinates.add(new Point2D.Float(66, 5));
                    roseCoordinates.add(new Point2D.Float(80, 3));
                    roseCoordinates.add(new Point2D.Float(9, 3));
                }
            }

            for (Point2D.Float roseCoordinate : roseCoordinates) {
                int tileSize = 32;
                int roseX = (int) (roseCoordinate.getX() * tileSize);
                int roseY = (int) (roseCoordinate.getY() * tileSize);

                Rose newRose = new Rose(roseX, roseY);
                rosesLevel.add(newRose);

                System.out.println("Trandafir creat la poziția: " + roseX + ", " + roseY);
            }
        } else {
            System.out.println("AVERTISMENT: Date nivel negăsite pentru inițializarea trandafirilor");
        }

        // Asigurăm-ne că adăugăm trandafirii la nivelul corect
        while (roses.size() <= lvlIndex) {
            roses.add(new ArrayList<>());
        }
        roses.set(lvlIndex, rosesLevel);
    }

    private void initHeart(int lvlIndex) {
        List<Heart> heartsLevel = new ArrayList<>();

        // Make sure we have level data to work with
        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {
            ArrayList<Point2D.Float> heartCoordinates = play.getLevelManager().getLevel(lvlIndex).getCoordinates(708);


            for (Point2D.Float heartCoordinate : heartCoordinates) {
                // Convert tile coordinates to pixel coordinates
                int heartX = (int) (heartCoordinate.getX() * 32);
                int heartY = (int) (heartCoordinate.getY() * 32);

                System.out.println("Creating heart at: " + heartX + ", " + heartY);
                heartsLevel.add(new Heart(heartX, heartY, 8));
            }
        } else {
            System.out.println("WARNING: Level data not found for hearts initialization");
        }

        hearts.add(heartsLevel);
    }


    private void initCar(int lvlIndex) {
        // Verificăm dacă avem date despre nivel
        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {

            ArrayList<Point2D.Float> carCoordinates = new ArrayList<>();

            // Mașina apare doar la nivelul 2 (ultimul nivel)
            if (lvlIndex == 2) {
                carCoordinates.add(new Point2D.Float(90, 20)); // Poziția mașinii
            }

            for (Point2D.Float carCoordinate : carCoordinates) {
                int tileSize = 32;
                int carX = (int) (carCoordinate.getX() * tileSize);
                int carY = (int) (carCoordinate.getY() * tileSize);

                Car newCar = new Car(carX, carY, 0);
                cars.add(newCar);

                System.out.println("Mașină creată la poziția: " + carX + ", " + carY + " pentru nivelul " + lvlIndex);
            }

            // Actualizăm referința către mașini în Player
            if (Play.getPlayer() != null) {
                Play.getPlayer().setCars(cars);
            }
        } else {
            System.out.println("AVERTISMENT: Date nivel negăsite pentru inițializarea mașinii");
        }
    }

    private void initDoor(int lvlIndex) {
        // Verificăm dacă avem date despre nivel
        if (play.getLevelManager() != null && play.getLevelManager().getLevel(lvlIndex) != null) {

            ArrayList<Point2D.Float> doorCoordinates = new ArrayList<>();

            // Adăugăm manual pozițiile ușilor pentru nivelul curent
            if (lvlIndex == 0) {
                // Poziții pentru nivelul 0 (coordonate în spațiul de tile-uri)
                doorCoordinates.add(new Point2D.Float(95, 17));  // Exemplu de coordonată pentru ușă
            } else if (lvlIndex == 1) {
                // Poziții pentru nivelul 1
                doorCoordinates.add(new Point2D.Float(93, 8));  // Exemplu de coordonată pentru ușă
            }
            // Pentru nivelul 2, nu mai adăugăm ușă, ci mașină

            // Creăm uși la pozițiile specificate
            for (Point2D.Float doorCoordinate : doorCoordinates) {
                // Convertim coordonatele de tile în coordonate de pixel
                int tileSize = 32;
                int doorX = (int) (doorCoordinate.getX() * tileSize);
                int doorY = (int) (doorCoordinate.getY() * tileSize);

                // Creăm ușa la poziția calculată
                Door newDoor = new Door(doorX, doorY, 0);
                doors.add(newDoor);
            }

            // Actualizăm referința către uși în Player
            if (Play.getPlayer() != null) {
                Play.getPlayer().setDoors(doors);
            }
        } else {
            System.out.println("AVERTISMENT: Date nivel negăsite pentru inițializarea ușilor");
        }
    }

    private void loadImages() throws ResourceNotFoundException {

        doorImages = new ArrayList<>();
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.DOOR);

        int doorWidth = 65;
        int doorHeight = 61;

        for (int i = 0; i < 5; i++) {
            doorImages.add(image.getSubimage(i * doorWidth, 0, doorWidth, doorHeight));
        }
        clockImage = LoadSave.GetSpriteAtlas(LoadSave.CLOCK);
        heartImage = LoadSave.GetSpriteAtlas(LoadSave.HEART_FULL);
        roseImage = LoadSave.GetSpriteAtlas(LoadSave.ROSE);
        galteraImage = LoadSave.GetSpriteAtlas(LoadSave.GALTERA);
        carImage = LoadSave.GetSpriteAtlas(LoadSave.CAR);
    }

    public void update() {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Update doors (for levels 0 and 1)
        if (currentLevel < 2 && doors.size() > currentLevel) {
            Door door = doors.get(currentLevel);
            if (door.isActive()) {
                door.update(currentLevel, play.getEnemyManager());

                // Check if player has touched the door
                if (Play.getPlayer() != null && Play.getPlayer().isDoorTouched()) {
                    play.setLevelCompleted(true);
                    System.out.println("Level completed detection in ObjectManager: " + Play.getPlayer().isDoorTouched());
                }
            }
        }

        // Update car (for level 2)
        if (currentLevel == 2 && !cars.isEmpty()) {
            for (Car car : cars) {
                if (car.isActive()) {
                    car.update(currentLevel, play.getEnemyManager());

                    // Check if player has touched the car
                    if (Play.getPlayer() != null && Play.getPlayer().isDoorTouched()) {
                        play.setLevelCompleted(true);
                        System.out.println("Level completed with car at level: " + currentLevel);
                    }
                }
            }
        }

        if (hearts.size() > currentLevel && !hearts.get(currentLevel).isEmpty()) {
            for (Heart heart : hearts.get(currentLevel)) {
                if (heart.isActive()) {
                    heart.update();
                }
            }
        }
        if (clocks.size() > currentLevel && !clocks.get(currentLevel).isEmpty()) {
            for (Clock clock : clocks.get(currentLevel)) {
                if (clock.isActive()) {
                    clock.update();
                }
            }
        }

        if (roses.size() > currentLevel && !roses.get(currentLevel).isEmpty()) {
            for (Rose rose : roses.get(currentLevel)) {
                if (rose.isActive()) {
                    rose.update();
                }
            }
        }
        if (galtere.size() > currentLevel && !galtere.get(currentLevel).isEmpty()) {
            for (Galtera galtera : galtere.get(currentLevel)) {
                if (galtera.isActive()) {
                    galtera.update();
                }
            }
        }

        checkObjectCollisions();
    }

    private void checkObjectCollisions() {
        if (Play.getPlayer() == null) return;

        Rectangle.Float playerHitbox = Play.getPlayer().getHitbox();
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Verifică coliziunile cu ceasurile
        if (clocks.size() > currentLevel && !clocks.get(currentLevel).isEmpty()) {
            for (Clock clock : clocks.get(currentLevel)) {
                if (clock.isActive() && playerHitbox.intersects(clock.getCollisionBox())) {
                    clock.setActive(false);
                    applyEffect(clock);
                }
            }
        }
        if (roses.size() > currentLevel && !roses.get(currentLevel).isEmpty()) {
            for (Rose rose : roses.get(currentLevel)) {
                if (rose.isActive() && playerHitbox.intersects(rose.getCollisionBox())) {
                    rose.setActive(false);
                    applyEffect(rose);
                }
            }
        }
        if (galtere.size() > currentLevel && !galtere.get(currentLevel).isEmpty()) {
            for (Galtera galtera : galtere.get(currentLevel)) {
                if (galtera.isActive() && playerHitbox.intersects(galtera.getCollisionBox())) {
                    galtera.setActive(false);
                    applyEffect(galtera);
                }
            }
        }
    }


    public void draw(Graphics g, int xLevelOffset) {
        drawDoor(g, xLevelOffset);
        drawCar(g, xLevelOffset);
        drawHearts(g, xLevelOffset);
        drawClocks(g, xLevelOffset);
        drawRoses(g, xLevelOffset);
        drawGaltera(g, xLevelOffset);
    }

    private void drawCar(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Desenează mașina doar la nivelul 2
        if (currentLevel == 2 && !cars.isEmpty()) {
            for (Car car : cars) {
                if (car.isActive()) {
                    // Folosește imaginea mașinii
                    g.drawImage(
                            carImage,
                            (int) (car.getCollisionBox().x - xLevelOffset),
                            (int) (car.getCollisionBox().y),
                            190, 100, // Dimensiunile mașinii
                            null
                    );

                    System.out.println("Drawing car at level " + currentLevel +
                            " at position: " + car.getCollisionBox().x + ", " + car.getCollisionBox().y);
                }
            }
        }
    }

    private void drawClocks(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Verificăm dacă avem ceasuri pentru nivelul curent
        if (clocks.size() > currentLevel && !clocks.get(currentLevel).isEmpty()) {
            for (Clock clock : clocks.get(currentLevel)) {
                if (clock.isActive()) {
                    // Desenăm imaginea ceasului
                    g.drawImage(
                            clockImage,
                            (int) (clock.getCollisionBox().x - xLevelOffset),
                            (int) (clock.getCollisionBox().y),
                            55, 55,  // Dimensiune - ajustați după nevoie
                            null
                    );

                    // Pentru debugging, puteți afișa cutia de coliziune
                    // clock.drawCollisionBox(g, xLevelOffset);
                }
            }
        }
    }

    private void drawRoses(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Verificăm dacă nivelul curent este 1 - nivelul cu trandafiri
        if (currentLevel == 1 && roses.size() > currentLevel && !roses.get(currentLevel).isEmpty()) {
            for (Rose rose : roses.get(currentLevel)) {
                if (rose.isActive()) {
                    // Desenăm imaginea trandafirului - folosește roseImage, nu clockImage
                    g.drawImage(
                            roseImage,
                            (int) (rose.getCollisionBox().x - xLevelOffset),
                            (int) (rose.getCollisionBox().y),
                            55, 55,  // Dimensiune - ajustați după nevoie
                            null
                    );
                }
            }
        }
    }

    private void drawGaltera(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Verificăm dacă nivelul curent este 2 - nivelul cu galtere
        if (currentLevel == 2 && galtere.size() > currentLevel && !galtere.get(currentLevel).isEmpty()) {
            for (Galtera galtera : galtere.get(currentLevel)) {
                if (galtera.isActive()) {
                    g.drawImage(
                            galteraImage,
                            (int) (galtera.getCollisionBox().x - xLevelOffset),
                            (int) (galtera.getCollisionBox().y),
                            99, 80,
                            null
                    );
                }
            }
        }
    }

    private void drawHearts(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Make sure we have hearts for the current level
        if (hearts.size() > currentLevel && !hearts.get(currentLevel).isEmpty()) {
            for (Heart heart : hearts.get(currentLevel)) {
                if (heart.isActive()) {
                    // Draw the heart image
                    g.drawImage(
                            heartImage,
                            (int) (heart.getCollisionBox().x - xLevelOffset),
                            (int) (heart.getCollisionBox().y),
                            32, 32,
                            null
                    );

                    // heart.drawCollisionBox(g, xLevelOffset);

                }
            }
        }
    }


    private void drawDoor(Graphics g, int xLevelOffset) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Desenează ușa doar pentru nivelurile 0 și 1
        if (currentLevel < 2 && doors.size() > currentLevel) {
            Door door = doors.get(currentLevel);
            int animIndex = door.isActive() ? door.getAnimationIndex() : 4;

            g.drawImage(doorImages.get(animIndex),
                    (int) (door.getCollisionBox().x - door.getxOffset() - xLevelOffset),
                    (int) (door.getCollisionBox().y - door.getxOffset()),
                    75, 98, null);
        }
    }

    public boolean playerReachedDoor() {
        if (Play.getPlayer() == null) {
            return false;
        }

        boolean touched = Play.getPlayer().isDoorTouched();
        int currentLevel = play.getLevelManager().getLevelIndex();

        // Pentru nivelurile 0 și 1, verifică ușa
        if (currentLevel < 2 && doors.size() > currentLevel) {
            Door door = doors.get(currentLevel);
            Rectangle2D.Float playerHitbox = Play.getPlayer().getHitbox();
            boolean intersects = playerHitbox.intersects(door.getHitbox());

            if (intersects && door.isFullyOpen() && !touched) {
                Play.getPlayer().setDoorTouched(true);
                touched = true;
                System.out.println("Jucătorul a atins ușa la nivelul " + currentLevel);
            }
        }
        // Pentru nivelul 2, verifică mașina
        else if (currentLevel == 2 && !cars.isEmpty()) {
            for (Car car : cars) {
                Rectangle2D.Float playerHitbox = Play.getPlayer().getHitbox();
                boolean intersects = playerHitbox.intersects(car.getHitbox());

                if (intersects && !touched) {
                    Play.getPlayer().setDoorTouched(true);
                    touched = true;
                    System.out.println("Jucătorul a atins mașina la nivelul " + currentLevel);
                    break;
                }
            }
        }

        if (touched) {
            // Marchează ca fiind atins
            Play.getPlayer().setDoorTouched(true);

            // Salvează jocul
            int levelIndex = play.getLevelManager().getLevelIndex();
            int hearts = Play.getPlayer().getHealth();
            int score = Play.getPlayer().getScore();
            int xPos = (int) Play.getPlayer().getHitbox().x;
            int yPos = (int) Play.getPlayer().getHitbox().y;

            DataBase db = Play.getDataBase();
            db.saveGame(levelIndex, hearts, score, xPos, yPos);

            System.out.println("Joc salvat după atingere - Nivel: " + levelIndex +
                    ", Inimi: " + hearts + ", Scor: " + score +
                    ", Poziție: (" + xPos + "," + yPos + ")");

            return true;
        }
        return touched;
    }


    public void checkObjectTouched(Rectangle.Float collisionBox) {
        int currentLevel = play.getLevelManager().getLevelIndex();

        if (clocks.size() > currentLevel) {
            for (Clock clock : clocks.get(currentLevel)) {
                if (clock.isActive() && collisionBox.intersects(clock.getCollisionBox())) {
                    clock.setActive(false);
                    applyEffect(clock);
                }
            }
        }
        if (roses.size() > currentLevel) {
            for (Rose rose : roses.get(currentLevel)) {
                if (rose.isActive() && collisionBox.intersects(rose.getCollisionBox())) {
                    rose.setActive(false);
                    applyEffect(rose);
                }
            }
        }
        if (galtere.size() > currentLevel) {
            for (Galtera galtera : galtere.get(currentLevel)) {
                if (galtera.isActive() && collisionBox.intersects(galtera.getCollisionBox())) {
                    galtera.setActive(false);
                    applyEffect(galtera);
                }
            }
        }
    }

    public void applyEffect(GameObject f) {
        if (f instanceof Heart) {
            if (Play.getPlayer() != null) {
                Play.getPlayer().updateHealth(1);
            }
        } else if (f instanceof Clock) {
            if (Play.getPlayer() != null) {
                // Adăugăm scor pentru colectarea ceasului
                int currentScore = Play.getPlayer().getScore();
                Play.getPlayer().setScore(currentScore + 50);  // Actualizăm scorul direct în player
                System.out.println("Scor actualizat: " + Play.getPlayer().getScore());
            }
        } else if (f instanceof Rose) {
            if (Play.getPlayer() != null) {
                // Add score for collecting a rose - different value than clocks
                int currentScore = Play.getPlayer().getScore();
                Play.getPlayer().setScore(currentScore + 100);  // More points for roses
                System.out.println("Scor actualizat după colectarea trandafirului: " + Play.getPlayer().getScore());
            }
        } else if (f instanceof Galtera) {
            if (Play.getPlayer() != null) {
                // Add score for collecting galtera
                int currentScore = Play.getPlayer().getScore();
                Play.getPlayer().setScore(currentScore + 150);  // Even more points for galtera
                System.out.println("Scor actualizat după colectarea galterii: " + Play.getPlayer().getScore());
            }
        }
    }

    public void resetAll() {
        doors.clear();
        cars.clear();

        // Reinițializează ușile pentru nivelurile 0 și 1
        initDoor(0);
        initDoor(1);

        // Reinițializează mașina pentru nivelul 2
        initCar(2);

        hearts.clear();
        initHeart(0);
        clocks.clear();
        initClocks(0);
        roses.clear();
        initRoses(1);
        galtere.clear();
        initGaltera(2);

        // Resetează starea doorTouched a jucătorului
        if (Play.getPlayer() != null) {
            Play.getPlayer().setDoorTouched(false);
        }
    }

    public List<Door> getDoors() {
        return doors;
    }

    public List<Car> getCars() {
        return cars;
    }
}