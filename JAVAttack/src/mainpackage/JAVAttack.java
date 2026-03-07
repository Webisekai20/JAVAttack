package mainpackage;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import javax.sound.sampled.FloatControl; 




public class JAVAttack extends JPanel implements ActionListener, KeyListener {
    Define def = new Define();
    Block ship;
    long powerupStartTime;
    final long POWERUP_DURATION = 15000;
    private long getRemainingTimeMs(ActivePowerup ap) {
        long now = System.currentTimeMillis();
        long elapsed = now - ap.getStartTime();        // use getter if you added it
        long remaining = POWERUP_DURATION - elapsed;
        return Math.max(remaining, 0);
    }
    // Game timer
    boolean gameStarted = false;
    Timer gameLoop;  
    int score = 0;
    int level = 1;
    boolean gameOver = false;   

    JAVAttack(){
        setPreferredSize(new Dimension(def.getBoardHeight(), def.getBoardWidth())); // windowsize/ screen size of the game
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        Assets.LoadAssets();
        ship = new Block(def.getShipX(), def.getShipY(), def.getShipWidth(), def.getShipHeight(), Assets.shipImg);

        // game timer
        gameLoop = new Timer(1000/60, this);
        createAliens();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){
        //start
        g.drawImage(Assets.background, 0, 0, def.getBoardWidth(), def.getBoardHeight(), this);
        Graphics2D g2d = (Graphics2D) g;

        // black overlay
        if (gameOver || !gameStarted) {
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.fillRect(0, 0, def.getBoardWidth(), def.getBoardHeight());
        }
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, def.getBoardWidth(), def.getBoardHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC, 20));

        if (!gameStarted) {
            g.drawString("Press Any Key to Start", def.getBoardWidth()/2-103, def.getBoardHeight()/2);
        }

        //ship
        g.drawImage(ship.getImg(), ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight(), null);  

        //aliens
        for(int i = 0; i < Assets.AlienArray.size(); i++){
            Block alien = Assets.AlienArray.get(i);
            if (alien.isAlive()) {
                g.drawImage(alien.getImg(), alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight(),  null);
            }
        }

        // bullets
        g.setColor(Color.GREEN);
        for(int i = 0; i < Assets.BulletArray.size(); i++){
            Block bullet = Assets.BulletArray.get(i);
            if(!bullet.isUsed()){
                g.fillRect(bullet.getX() - def.getOffset(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
            }
        }
        g.setColor(Color.RED);
        for(int i = 0; i < Assets.AlienBullets.size(); i++){
            Block bullet = Assets.AlienBullets.get(i);
            if(!bullet.isUsed()){
                g.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
            }
        }

        // powerups
        for (int i = 0; i < Assets.PowerupArray.size(); i++) {
            Block p = Assets.PowerupArray.get(i);
            g.drawImage(p.getImg(), p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));

        int textY = 70;          // start below the level text
        int lineStep = 16;       // vertical spacing

        for (int ndx = 0; ndx < Assets.ActivePowerupsArr.size(); ndx++) {
            ActivePowerup ap = Assets.ActivePowerupsArr.get(ndx);
            if (!ap.getIsActive()) continue;

            long remainingMs = getRemainingTimeMs(ap);
            if (remainingMs <= 0) continue;        // do NOT draw if timer hit 0
            int seconds = (int)(remainingMs / 1000);
            

            String label = "";
               switch (ap.aType) {
                case BulletSpeedInc: label = "Bullet Speed"; break;
                case SizeInc: label = "Attack Size";  break;
                case BoostInc: label = "Score Boost";  break;
                case AttackSpeedInc: label = "Attack Speed"; break;
            }

            g.drawString(label + ": " + seconds + "s", 10, textY);
            textY += lineStep;
        }

        // score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        if(gameOver){
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.drawString("GAME OVER!", def.getBoardWidth()/2 - 125, def.getBoardHeight()/2-40);
            g2d.setFont(new Font("Arial", Font.ITALIC, 20));
            g2d.drawString("Total Score: " + String.valueOf(score),def.getBoardWidth()/2 - 65, def.getBoardHeight()/2);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("Press Any Key to Start", def.getBoardWidth()/2-55, def.getBoardHeight()/2+80);
        }
        else if (gameStarted){
            g.drawString("Score: " + String.valueOf(score), 10, 30);
            g.setFont(new Font("Arial", Font.ITALIC, 15));
            g.drawString("Level: " + level, 10, 50);
        }

        // boss
        if (def.isBossAlive() && def.getBoss() != null) {
            g.drawImage(def.getBoss().getImg(), def.getBoss().getX(), def.getBoss().getY(), def.getBoss().getWidth(), def.getBoss().getHeight(), null);

            // boss health
            g.setColor(Color.RED);
            g.fillRect(def.getBoss().getX(), def.getBoss().getY() - 10, def.getBoss().getWidth(), 5);
            g.setColor(Color.GREEN);
            g.fillRect(def.getBoss().getX(), def.getBoss().getY() - 10, def.getBoss().getWidth() * def.getBossHealth() / (20 + level * 2), 5);
        }
    }

    public void move(){
        //aliens
        int random;
        int powerupChance;
        Random rand = new Random();
        for(int i = 0; i < Assets.AlienArray.size(); i++){
            Block alien = Assets.AlienArray.get(i);
            if(alien.isAlive()){
                alien.setX(alien.getX() + def.getAlienVelocityX());

                random = rand.nextInt(100 + (100 * level)) + 1;

                if(random == 1){  // chances are 1/100 frames. change the param in rand.nextInt() to change the chances
                    Assets.AlienBullets.add(new Block(alien.getX() + def.getAlienWidth()*15/32, alien.getY(),  def.getBulletWidth(), def.getBulletHeight(), null));
                }

                if (alien.getX() + alien.getWidth() >= def.getBoardWidth() || alien.getX() <=0) {
                    def.setAlienVelocityX(def.getAlienVelocityX() * -1);
                    alien.setX(alien.getX() + def.getAlienVelocityX()*2);

                    //move all aliens down by one row
                    for(int j = 0; j < Assets.AlienArray.size(); j++){
                        Assets.AlienArray.get(j).setY(Assets.AlienArray.get(j).getY() + def.getAlienHeight());
                    }
                }
                if(alien.getY() >= ship.getY()){
                    gameOver = true;
                }
            }
        }
        //bullets
        for(int i = 0; i < Assets.BulletArray.size(); i++ ){
            Block bullet = Assets.BulletArray.get(i);
            bullet.setY(bullet.getY() + def.getBulletVelocityY()- def.getPowerVelocity());
            // bullet collision with aliens
            for(int j = 0; j < Assets.AlienArray.size(); j++){
                Block alien = Assets.AlienArray.get(j);
                if(!bullet.isUsed() && alien.isAlive() && detectCollision(bullet, alien)){
                    bullet.setUsed( true);
                    alien.setAlive( false);
                    def.setAlienCount(def.getAlienCount() -1);
                    score += 50*level * def.getScoreBoost(); 
                    powerupChance = rand.nextInt(10); // powerup chance
                    if (powerupChance == 0) {
                        createPowerup(alien.getX(), alien.getY());
                    }

                    if (Assets.getDeadSound() != null) {
                        Assets.getDeadSound().setFramePosition(0);
                        Assets.getDeadSound().start();
                    }
                }
            }
        }
        for(int i = 0;i < Assets.AlienBullets.size(); i++ ){
            Block bullet = Assets.AlienBullets.get(i);
            bullet.setY(bullet.getY() - def.getBulletVelocityY());
            // bullet collision with aliens
            if(detectCollision(bullet, ship)){
                bullet.setUsed(true);
                gameOver = true;
                    if (Assets.getGameOverSound() != null) {
                    Assets.getGameOverSound().setFramePosition(0);
                    Assets.getGameOverSound().start();
                }
            }
        }
        // powerups
        for (int i = 0; i < Assets.PowerupArray.size(); i++){
            Block p = Assets.PowerupArray.get(i);
            if(ship.getY() > p.getY()){
                p.setY(p.getY() + 4);
            }

            if (detectCollision(p, ship)){
                activatePowerup(p.getType());
                Assets.PowerupArray.remove(i);
                i--;
                continue;

            }else if (p.getY() > def.getBoardHeight()){
                Assets.PowerupArray.remove(i);
                i--;
            }
            for (int ndx = 0; ndx < 4; ndx++) {
                ActivePowerup ap = Assets.ActivePowerupsArr.get(ndx);
                if (ap.getIsActive() && getRemainingTimeMs(ap) == 0) {
                    deactivatePowerup(ndx);
                }
            }
        }
        //populate ActivePowerupsArr
        for(PowerupType ndx : PowerupType.values()){
            ActivePowerup deactivated = new ActivePowerup(ndx, 0, false);
            Assets.ActivePowerupsArr.add(deactivated);
        }
        // clear out of screen bullets
        while(Assets.BulletArray.size() >0 && (Assets.BulletArray.get(0).isUsed() || Assets.BulletArray.get(0).getY() < 0)){
            Assets.BulletArray.remove(0);
        }
        while(Assets.AlienBullets.size() >0 && (Assets.AlienBullets.get(0).isUsed() || Assets.AlienBullets.get(0).getY() < 0)){
            Assets.AlienBullets.remove(0);
        }
        // next level
        if(def.getAlienCount() == 0 && !def.isBossAlive()){
            // increase aliens
            level++;
            // if boss level
            if (level % 5 == 0) {
                createBoss();
                if (Assets.getBackgroundMusic() != null) {
                    Assets.getBackgroundMusic().stop();
                    Assets.getBossBackgroundMusic().start();
                }
            } else{
                def.setAlienColumn( Math.min(def.getAlienColumn() +1, def.getColumn()/2-2));
                def.setAlienRows( Math.min(def.getAlienRows()+1, def.getRow() - 6));
                Assets.AlienArray.clear();
                Assets.BulletArray.clear();
                Assets.AlienBullets.clear();
                def.setAlienVelocityX (4);
                createAliens();
                if (Assets.getNewLevelSound() != null) {
                    Assets.getNewLevelSound().setFramePosition(0);
                    Assets.getNewLevelSound().start();
                }
                if (level > 5) {
                    Assets.getBossBackgroundMusic().stop();
                    Assets.getBackgroundMusic().loop(Clip.LOOP_CONTINUOUSLY);
                    // backgroundMusic.start();
                }       
            }
        }
        // boss attack
        if (def.isBossAlive() && def.getBoss() != null) {
            Random bossBullet = new Random();
            int targetX = ship.getX() + ship.getWidth() / 2;
            int fireRate = Math.max(8, 40 - level * 3);
            int randomX = bossBullet.nextInt(def.getBoardWidth() - def.getBulletWidth());
            def.getBoss().setX(def.getBoss().getX() + def.getBossVelocityX());
            if (def.getBoss().getX() <= 0 || def.getBoss().getX() + def.getBoss().getWidth() >= def.getBoardWidth()) {
                def.setBossVelocityX(def.getBossVelocityX() * -1);
            }
            if (bossBullet.nextInt(fireRate) == 1) {
                for (int i = 0; i < def.getBoardWidth(); i += def.getTileSize() * 4) {
                    Assets.AlienBullets.add(new Block(i, def.getBoss().getY() + def.getBoss().getHeight(), def.getBulletWidth(), def.getBulletHeight(), null));
                }
                Assets.AlienBullets.add(new Block(randomX, def.getBoss().getY() + def.getBoss().getHeight(), def.getBulletWidth(), def.getBulletHeight(), null));
                Assets.AlienBullets.add(new Block(targetX, def.getBoss().getY() + def.getBoss().getHeight(), def.getBulletWidth(), def.getBulletHeight(), null));
            }
        }
        // boss damage and death
        if (def.isBossAlive() && def.getBoss() != null) {
            for (int i = 0; i < Assets.BulletArray.size(); i++) {
                Block bullet = Assets.BulletArray.get(i);
                if (!bullet.isUsed() && detectCollision(bullet, def.getBoss())) {
                    bullet.setUsed( true);
                    def.setBossHealth(def.getBossHealth() - 1);
                    if (def.getBossHealth() <= 0) {
                        def.setBossAlive(false);
                        def.setBoss(null);
                        score += 500 * level;
                        def.setAlienCount(0) ;
                        i = Assets.BulletArray.size() + 1;
                    }
                }
            }
        }
    }

    public void createBoss() {
        Block boss = new Block(
            def.getBoardWidth() / 2 - def.getBossWidth() / 2,
            def.getTileSize(),
            def.getBossWidth(),
            def.getBossHeight(),
            Assets.alienYellowImg
        );
        def.setBoss(boss);
        def.setBossAlive(true);
        def.setBossHealth(20 + (level * 2));
    }


    public void createAliens(){
        Random random = new Random();
        for (int r = 0; r< def.getAlienRows(); r++){
            for(int c = 0; c < def.getAlienColumn(); c++){
                int randomImgIndex = random.nextInt(Assets.alienImgArray.size());
                Block alien = new Block(
                    def.getAlienX() + c*def.getAlienWidth(),
                    def.getAlienY() + r*def.getAlienHeight(),
                    def.getAlienWidth(),
                    def.getAlienHeight(),
                    Assets.alienImgArray.get(randomImgIndex)
                );
                Assets.AlienArray.add(alien);
            }
        }
        def.setAlienCount(Assets.AlienArray.size());
    }

    public void activatePowerup(PowerupType type){
        int count = 0;
        for(PowerupType ndx : PowerupType.values()){
            if(type == ndx && !Assets.ActivePowerupsArr.get(count).getIsActive()){
                powerupStartTime = System.currentTimeMillis();
                boolean powerupActive = true;
                ActivePowerup newPow = new ActivePowerup(
                    type,
                    powerupStartTime,
                    powerupActive
                );
                Assets.ActivePowerupsArr.add(count, newPow);
            }else if(type == ndx && Assets.ActivePowerupsArr.get(count).getIsActive()){
                Assets.ActivePowerupsArr.get(count).setStartTime(System.currentTimeMillis());
            }
            count++;
        }
        switch(type){
            case BulletSpeedInc:
                def.setPowerVelocity(4);
                break;
            case SizeInc:
                def.setAttackSize(def.getTileSize());
                def.setOffset(16);
                break;
            case BoostInc:
                def.setScoreBoost(2);
                break;
            case AttackSpeedInc:
                def.setBufferTimeBoost(200);
                break;
        }
    }

    public void deactivatePowerup(int deact){
        switch(Assets.ActivePowerupsArr.get(deact).aType){
            case BulletSpeedInc:
                def.setPowerVelocity(0);
                break;
            case SizeInc:
                def.setAttackSize(0);
                def.setOffset(0);
                break;
            case BoostInc:
                def.setScoreBoost(1);
                break;
            case AttackSpeedInc:
                def.setBufferTimeBoost(0);
                break;
        }
        Assets.ActivePowerupsArr.get(deact).setIsActive(false);
    }


    public void createPowerup(int x, int y){
        Random random = new Random();
        int index = random.nextInt(Assets.PowerupImgArray.size());

        Block powerup = new Block(
            x,
            y,
            def.getPowerWidth(),
            def.getPowerHeight(),
            Assets.PowerupImgArray.get(index)
        );
        powerup.setType(PowerupType.values()[index]);
        
        Assets.PowerupArray.add(powerup);
    }

    public boolean detectCollision(Block a, Block b){
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }

    private void moveShip(){
        if(def.isLeft() || def.isRight()){
            if (def.isLeft() && ship.getX() - def.getShipVelocityX() >= 5) {
                ship.setX(ship.getX() - def.getShipVelocityX());
            }
            if(def.isRight() && ship.getX() + ship.getWidth() + def.getShipVelocityX() <= def.getBoardWidth() - 5){
                ship.setX(ship.getX() + def.getShipVelocityX());
            }   
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       move();
       moveShip(); 
       repaint();
        
        if(gameOver){
            gameLoop.stop();
            if (Assets.getBackgroundMusic() != null) {
                Assets.getBackgroundMusic().stop();
            }
             if(Assets.getBackgroundMusic() != null){
                Assets.getBossBackgroundMusic().stop();
            }
            for(int ndx = 0; ndx < Assets.ActivePowerupsArr.size(); ndx++){
                deactivatePowerup(ndx);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameStarted) {
        gameStarted = true;
        gameLoop.start();

        if (Assets.getBackgroundMusic() != null) {
            Assets.getBackgroundMusic().loop(Clip.LOOP_CONTINUOUSLY);
        }
        
    }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.getX() - def.getShipVelocityX() >= 0) {
            def.setLeft(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.getX() + ship.getWidth() + def.getShipVelocityX() <= def.getBoardWidth()){
            def.setRight(true);
        }
    }


        

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.getX() - def.getShipVelocityX() >= 0) {
            def.setLeft(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.getX() + ship.getWidth() + def.getShipVelocityX() <= def.getBoardWidth()){
            def.setRight(false);
        }
        if(gameOver){

            ship.setX(def.getShipX());
            Assets.AlienArray.clear();
            Assets.BulletArray.clear();
            Assets.AlienBullets.clear();
            Assets.PowerupArray.clear();
            def.setBoss(null);
            def.setBossAlive(false);
            score = 0;
            def.setAlienVelocityX (4) ;
            def.setAlienColumn(3);
            def.setAlienRows(2);
            level = 1;
            gameOver = false;
            createAliens();
            gameLoop.start();
            if (Assets.getBackgroundMusic() != null) {
                Assets.getBackgroundMusic().setFramePosition(0);
                Assets.getBackgroundMusic().loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        else if((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) &&  System.currentTimeMillis() - def.getShootBuffer() > def.getBufferTime() - def.getBufferTimeBoost()){
            Block bullet  = new Block(ship.getX() + def.getShipWidth()*15/32, ship.getY(), def.getBulletWidth() + def.getAttackSize(), def.getBulletHeight(), null);
            Assets.BulletArray.add(bullet);
            def.setShootBuffer(System.currentTimeMillis());
               //   Play bullet sound
            if (Assets.getBulletSound() != null) {
                Assets.getBulletSound().setFramePosition(0);
                Assets.getBulletSound().start();
           }
        }
    }
}   

