
// main.GamePanel

package main;
import entity.Player;
import graphicObject.Ground;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3; // to scale the tile since our computer resolution is high
    final int tileSize = originalTileSize * scale; // 48 x 48 tile

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;
    private Thread gameThread;

    // graphics
    private TileManager tileManager = new TileManager(this);
    private Ground groundGraphics = new Ground(this);

    // Game Logic Managers
    KeyHandler keyHandler = new KeyHandler();
    EncounterManager encounterManager = new EncounterManager(this);

    // player
    Player player = new Player(this, keyHandler);

    // game data
    public double timer = 0;
    public int groundScrollSpeed = 2;


    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(new Color(145,240,255));
        setDoubleBuffered(true); // for better rendering performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // allow to be panel to be focused to get key input
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    // The game loop
    public void run() {

        // 1/60 sec
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            // increment all the timers
            timer += ((currentTime - lastTime) / drawInterval) / FPS;
            if(encounterManager.shouldSpawnEncounter){
                encounterManager.timer += ((currentTime - lastTime) / drawInterval) / FPS;
            }
            lastTime = currentTime;

            if(delta >= 1) {
                // 1. UPDATE: update information
                UPDATE();
                // 2. DRAW: draw gui and graphics
                repaint();
                delta--;
            }

        }
    }

    public void UPDATE(){

        groundGraphics.update();
        encounterManager.update();
        player.update();

    }

    // DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // tileManager.draw(g2);
        groundGraphics.draw(g2);
        encounterManager.draw(g2);
        player.draw(g2);
        // GUI
        g2.drawString(Integer.toString((int)timer), 10, 10);

        // when drawing is done, dispose to save memory
        g2.dispose();

    }

    // getters
    public int getMaxScreenCol() {
        return maxScreenCol;
    }
    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    public int getScreenWidth(){
        return screenWidth;
    }
    public int getScreenHeight(){
        return screenHeight;
    }
    public int getScale(){
        return scale;
    }
    public int getTileSize(){
        return tileSize;
    }
}
