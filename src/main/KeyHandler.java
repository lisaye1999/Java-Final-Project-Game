package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, confirmPressed, cancelPressed;
    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // TITLE
        if(gp.gameState == GamePanel.GameState.TITLE){
            if (key == KeyEvent.VK_UP) {
                gp.playSFX(Sound.CURSOR_SFX);
                gp.ui.titleOptionPos--;
                if(gp.ui.titleOptionPos < 0) {
                    gp.ui.titleOptionPos = 1;
                }
            }
            if (key == KeyEvent.VK_DOWN) {
                gp.playSFX(Sound.CURSOR_SFX);
                gp.ui.titleOptionPos++;
                gp.ui.titleOptionPos %= 2;
            }
            if (key == KeyEvent.VK_ENTER) {
                // new game
                if(gp.ui.titleOptionPos == 0){
                    gp.gameState = GamePanel.GameState.GAME_PLAY;
                    gp.playMusic(Sound.MAIN_BGM);
                }
                // load game
                else{
                    gp.loadGame();
                    gp.gameState = GamePanel.GameState.GAME_PLAY;
                    gp.playMusic(Sound.MAIN_BGM);
                }
            }
        }
        else if(gp.gameState == GamePanel.GameState.GAME_PLAY) {
            if (key == KeyEvent.VK_E) {
                gp.eatBread();
            }
        }
        else if(gp.gameState == GamePanel.GameState.ENCOUNTER_STATE){
            switch(gp.encounterManager.encounter){
                case INN:
                    if (key == KeyEvent.VK_Z) {
                        gp.playSFX(Sound.KEY_SFX);
                        gp.saveGame();
                        gp.exitEncounter();
                    }
                    break;
                case SHOP:
                    // buy bread
                    if (key == KeyEvent.VK_1) {
                        if(gp.gold >= gp.BREAD_BUY_PRICE){
                            gp.playSFX(Sound.TRANSACTION_SFX);
                            gp.gold -= gp.BREAD_BUY_PRICE;
                            gp.bread++;
                        }
                        else{
                            gp.playSFX(Sound.ERROR_SFX);
                            gp.ui.showMessage("I don't have enough gold.");
                        }
                    }
                    // buy flower
                    if (key == KeyEvent.VK_2) {
                        if(gp.gold >= gp.FLOWER_BUY_PRICE){
                            gp.playSFX(Sound.TRANSACTION_SFX);
                            gp.gold -= gp.FLOWER_BUY_PRICE;
                            gp.flower++;
                        }
                        else{
                            gp.playSFX(Sound.ERROR_SFX);
                            gp.ui.showMessage("I don't have enough gold.");
                        }
                    }
                    // sell flower
                    if (key == KeyEvent.VK_3) {
                        if(gp.flower > 0){
                            gp.playSFX(Sound.TRANSACTION_SFX);
                            gp.gold += gp.FLOWER_SELL_PRICE;
                            gp.flower--;
                        }
                        else{
                            gp.playSFX(Sound.ERROR_SFX);
                            gp.ui.showMessage("I don't have flowers.");
                        }
                    }
                    if (key == KeyEvent.VK_X) {
                        gp.playSFX(Sound.KEY_SFX);
                        gp.exitEncounter();
                    }
                    break;
                case EVENT:
                    if(gp.encounterManager.currEvent.isTransaction){
                        if (key == KeyEvent.VK_Z) {
                            gp.encounterManager.handleTransactionEvent(gp.encounterManager.currEvent);
                            gp.exitEncounter();
                        }
                        if (key == KeyEvent.VK_X) {
                            gp.playSFX(Sound.KEY_SFX);
                            gp.exitEncounter();
                        }
                    }
                    else{
                        if (key == KeyEvent.VK_Z || key == KeyEvent.VK_X) {
                            gp.playSFX(Sound.KEY_SFX);
                            gp.encounterManager.handleEvent(gp.encounterManager.currEvent);
                            gp.exitEncounter();
                        }
                    }
                    break;
            }
            if (key == KeyEvent.VK_X) {
                gp.playSFX(Sound.KEY_SFX);
                gp.exitEncounter();
            }
            if (key == KeyEvent.VK_E) {
                gp.eatBread();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(key == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(key == KeyEvent.VK_Z){
            confirmPressed = false;
        }
        if(key == KeyEvent.VK_X){
            cancelPressed = false;
        }

    }
}
