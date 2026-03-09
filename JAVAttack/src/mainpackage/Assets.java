package mainpackage;

import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;

public class Assets {
    public static ImageIcon bgImg;
    public static Image background;
    public static Image shipImg;
    public static Image alienImg;
    public static Image alienCyanImg;
    public static Image alienMagentaImg;
    public static Image alienYellowImg;
    public static ArrayList<Image> alienImgArray;
    public static BufferedImage bulletSpeedIncImg = null;
    public static BufferedImage sizeIncImg = null;
    public static BufferedImage boostIncImg = null;
    public static BufferedImage attackSpeedIncImg = null;
    public static ArrayList<BufferedImage> PowerupImgArray;
    // arrays
    public static ArrayList<Block> AlienArray;
    public static ArrayList<Block> BulletArray;
    public static ArrayList<Block> AlienBullets;
    public static ArrayList<Block> PowerupArray;
    public static ArrayList<ActivePowerup> ActivePowerupsArr;

    // Sounds
    private static Clip backgroundMusic;
    private static Clip bossBackgroundMusic;
    private static Clip bulletSound;
    private static Clip deadSound;
    private static Clip gameOverSound;
    private static Clip newLevelSound;
    private static Clip mainMenuMusic;
    private static Clip pauseEffect;
    private static Clip quitEffect;
    public static Clip getBackgroundMusic() {return backgroundMusic;}
    public static Clip getBossBackgroundMusic() {return bossBackgroundMusic;}
    public static Clip getBulletSound() {return bulletSound;}
    public static Clip getDeadSound() {return deadSound;}
    public static Clip getGameOverSound() {return gameOverSound;}
    public static Clip getNewLevelSound() {return newLevelSound;}
    public static Clip getMainMenuMusic() {return mainMenuMusic;}
    public static Clip getPauseEffect() {return pauseEffect;}
    public static Clip getQuitEffect() {return quitEffect;}

    public static void LoadAssets() {
        //load images
        bgImg = new ImageIcon(Assets.class.getResource("/assets/img/StarryBg.gif"));
        background = bgImg.getImage();
        shipImg = new ImageIcon(Assets.class.getResource("/assets/img/ship.png")).getImage();
        alienImg = new ImageIcon(Assets.class.getResource("/assets/img/alien.png")).getImage();
        alienCyanImg = new ImageIcon(Assets.class.getResource("/assets/img/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(Assets.class.getResource("/assets/img/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(Assets.class.getResource("/assets/img/alien-yellow.png")).getImage();
        try {
            bulletSpeedIncImg = ImageIO.read(Assets.class.getResource("/assets/img/sniper.png"));
            sizeIncImg = ImageIO.read(Assets.class.getResource("/assets/img/lazerGun.png"));
            boostIncImg = ImageIO.read(Assets.class.getResource("/assets/img/speedUp.png"));
            attackSpeedIncImg = ImageIO.read(Assets.class.getResource("/assets/img/machineGun.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        PowerupImgArray = new ArrayList<BufferedImage>();
        PowerupImgArray.add(bulletSpeedIncImg);
        PowerupImgArray.add(sizeIncImg);
        PowerupImgArray.add(boostIncImg);
        PowerupImgArray.add(attackSpeedIncImg);

        //polymorphism
        
        AlienArray = new ArrayList<Block>();
        BulletArray = new ArrayList<Block>();
        AlienBullets = new ArrayList<Block>();
        PowerupArray = new ArrayList<Block>();
        ActivePowerupsArr = new ArrayList<ActivePowerup>();
         //populate ActivePowerupsArr
        for(PowerupType ndx : PowerupType.values()){
            ActivePowerup deactivated = new ActivePowerup(ndx, 0, false);
            ActivePowerupsArr.add(deactivated);
        }
            
        try {
            // Load main menu music
            AudioInputStream mainMenu = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/music/retro.wav"));
            mainMenuMusic = AudioSystem.getClip();
            mainMenuMusic.open(mainMenu);
            //  Load ingame main background music
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/music/console.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);  
            //  Load boss background music
            AudioInputStream boss = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/music/chad.wav"));
            bossBackgroundMusic = AudioSystem.getClip();
            bossBackgroundMusic.open(boss);
            // Load bullet sound
            AudioInputStream bullet = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/shootShip2.wav"));
            bulletSound = AudioSystem.getClip();
            bulletSound.open(bullet);
            // Load alien death sound effect
            // AudioInputStream alien = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/fahhh.wav"));
            AudioInputStream alien = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/destroyAlien2.wav"));
            deadSound = AudioSystem.getClip();
            deadSound.open(alien);
             // Load game over sound effect
            AudioInputStream bulletAudioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/destroyAlien.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(bulletAudioInputStream);            
            // Load new Level sound effect
            AudioInputStream newLevel = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/newLevel.wav"));
            newLevelSound = AudioSystem.getClip();
            newLevelSound.open(newLevel);
            // pause
            AudioInputStream pause = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/pause.wav"));
            pauseEffect = AudioSystem.getClip();
            pauseEffect.open(pause);
            //quit
            AudioInputStream quit = AudioSystem.getAudioInputStream(Assets.class.getResource("/assets/effects/quit.wav"));
            quitEffect = AudioSystem.getClip();
            quitEffect.open(quit);


             // Get the volume control from the clip
            FloatControl backgroundVolumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            // Set volume (in decibels)
            // Range is usually from -80.0 (mute) to 6.0 (max boost)
            backgroundVolumeControl.setValue(0f); // Example: reduce volume
            FloatControl mainMenuVolumeControl = (FloatControl) mainMenuMusic.getControl(FloatControl.Type.MASTER_GAIN);
            mainMenuVolumeControl.setValue(+6.0f);
            FloatControl bossBackgroundVolumeControl = (FloatControl) bossBackgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            bossBackgroundVolumeControl.setValue(0f); 
            FloatControl bulletvolumeControl = (FloatControl) bulletSound.getControl(FloatControl.Type.MASTER_GAIN);
            bulletvolumeControl.setValue(+6f); 
            FloatControl deathvolumeControl = (FloatControl) deadSound.getControl(FloatControl.Type.MASTER_GAIN);
            deathvolumeControl.setValue(+6f);
            FloatControl overVolumeControl = (FloatControl) gameOverSound.getControl(FloatControl.Type.MASTER_GAIN);
            overVolumeControl.setValue(+6f);
            FloatControl newLevelVolumeControl = (FloatControl) newLevelSound.getControl(FloatControl.Type.MASTER_GAIN);
            newLevelVolumeControl.setValue(+4f);
            FloatControl pauseVolumeControl = (FloatControl) pauseEffect.getControl(FloatControl.Type.MASTER_GAIN);
            pauseVolumeControl.setValue(+5f);
            FloatControl quitVolumeControl = (FloatControl) quitEffect.getControl(FloatControl.Type.MASTER_GAIN);
            quitVolumeControl.setValue(-3f);


            

            
            // mainMenuVolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // backgroundVolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // bossBackgroundVolumeControl.setValue(mainMenuVolumeControl.getMinimum()); 
            // bulletvolumeControl.setValue(mainMenuVolumeControl.getMinimum()); 
            // deathvolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // overVolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // newLevelVolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // pauseVolumeControl.setValue(mainMenuVolumeControl.getMinimum());
            // quitVolumeControl.setValue(mainMenuVolumeControl.getMinimum());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
