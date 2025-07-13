package PaooGame.GameStates;

import PaooGame.Graphics.LoadSave;
import PaooGame.Main.Game;
import PaooGame.UI.MenuButtons;
import PaooGame.Exceptions.ResourceNotFoundException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods{

    private MenuButtons[] buttons = new MenuButtons[3];
    private BufferedImage backgroundImg, backgroundImgMenu;
    private int menuX, menuY, menuWidth, menuHeight;
    private boolean resourcesLoaded = false;

    public Menu(Game game) throws ResourceNotFoundException {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadButtons() throws ResourceNotFoundException {
        int buttonYStart = (int) (Game.GAME_HEIGHT / 1.6f);// Start buttons from middle of screen
        int buttonXStart = (int) (Game.GAME_WIDTH * 0.75f); //
        int buttonSpacing = (int) (75 * Game.SCALE);

        buttons[0] = new MenuButtons(buttonXStart, buttonYStart, 0, GameState.PLAY);
        buttons[1] = new MenuButtons(buttonXStart, buttonYStart + buttonSpacing, 1, GameState.LOAD);
        buttons[2] = new MenuButtons(buttonXStart, buttonYStart + buttonSpacing * 2, 2, GameState.QUIT);
    }

    private void loadBackground() {
        try {
            // Încarcă imaginea de fundal principală
            backgroundImgMenu = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);

            // Încarcă imaginea de fundal pentru meniu
            backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);

            // Setează dimensiunile și pozițiile
            menuWidth = (int) (500 * Game.SCALE);
            menuHeight = (int) (400 * Game.SCALE);
            menuX = (int) (Game.GAME_WIDTH * 0.6f);
            menuY = (int) (Game.GAME_HEIGHT * 0.48f); // Position background near top

            resourcesLoaded = true;

        } catch (ResourceNotFoundException e) {
            System.err.println("Eroare la încărcarea resurselor pentru meniu: " + e.getMessage());

            // Creează imagini de rezervă sau setează flag-ul pentru desenare alternativă
            createFallbackImages();
            resourcesLoaded = false;
        }
    }

    private void createFallbackImages() {
        // Creează imagini simple de rezervă dacă încărcarea eșuează
        int width = Game.GAME_WIDTH;
        int height = Game.GAME_HEIGHT;

        // Imagine de fundal simplă
        backgroundImgMenu = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImgMenu.createGraphics();
        g2d.setColor(new Color(50, 50, 100)); // Albastru închis
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        // Imagine pentru panoul de meniu
        menuWidth = (int) (500 * Game.SCALE);
        menuHeight = (int) (400 * Game.SCALE);
        menuX = (int) (Game.GAME_WIDTH * 0.6f);
        menuY = (int) (Game.GAME_HEIGHT * 0.48f);

        backgroundImg = new BufferedImage(menuWidth, menuHeight, BufferedImage.TYPE_INT_RGB);
        g2d = backgroundImg.createGraphics();
        g2d.setColor(new Color(100, 100, 150)); // Albastru mai deschis
        g2d.fillRect(0, 0, menuWidth, menuHeight);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(2, 2, menuWidth - 4, menuHeight - 4);
        g2d.dispose();
    }

    @Override
    public void draw(Graphics g) {
        // Desenează fundalul principal
        if (backgroundImgMenu != null) {
            g.drawImage(backgroundImgMenu, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        } else {
            // Desenare de rezervă dacă imaginea nu există
            g.setColor(new Color(50, 50, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        }

        // Desenează panoul de meniu
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        } else {
            // Desenare de rezervă pentru panoul de meniu
            g.setColor(new Color(100, 100, 150));
            g.fillRect(menuX, menuY, menuWidth, menuHeight);
            g.setColor(Color.WHITE);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawRect(menuX + 2, menuY + 2, menuWidth - 4, menuHeight - 4);
        }

        // Afișează mesaj dacă resursele nu s-au încărcat corect
        if (!resourcesLoaded) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Avertisment: Unele resurse nu s-au încărcat corect!", 10, 30);
        }

        // Desenează butoanele
        for (MenuButtons mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void update() {
        for (MenuButtons mb : buttons)
            mb.update();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Implementare goală
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButtons mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGamestate();
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButtons mb : buttons)
            mb.resetBooleans();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButtons mb : buttons)
            mb.setMouseOver(false);

        for (MenuButtons mb : buttons)
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButtons mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            GameState.state = GameState.PLAY;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Implementare goală
    }
}