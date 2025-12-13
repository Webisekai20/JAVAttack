import java.awt.*;
import java.awt.event.*;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class JAVAttack extends JPanel implements ActionListener, KeyListener {
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true;// aliens
        boolean used = false; // bullets
        
        Block(int x, int y,  int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //board
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;



    //ship
    int shipWidth = tileSize*2;  // 64
    int shipHeight = tileSize;  ///32
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = tileSize;
    Block ship;

    //aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2;
    int alineColumn = 3;
    int alienCount = 0; // num of aliens to defeat
    int alienVelocityX = 1;

    // bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10;// moving speed


    Timer gameLoop;  
    int score = 0;
    int level = 1;
    boolean gameOver = false; 

    JAVAttack(){
        setPreferredSize(new Dimension(boardHeight, boardWidth));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("/alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        // game timer
        gameLoop = new Timer(1000/60, this);
        createAliens();
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){
        //ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);  

        //aliens
        for(int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if (alien.alive ) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height,  null);
            }
        }

        // bullets
        g.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < bulletArray.size(); i++){
            Block bullet = bulletArray.get(i);
            if(!bullet.used){
                //g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // score
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf(score),10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }

    }

    public void move(){
        //aliens
        for(int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                alien.x += alienVelocityX;

                if (alien.x + alien.width >= boardWidth || alien.x <=0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;

                    //move all aliens down by one row
                    for(int j = 0; j < alienArray.size(); j++){
                        alienArray.get(j).y += alienHeight;
                    }
                }
                if(alien.y >= ship.y){
                    gameOver = true;
                }
            }
        }
        //bullets
        for(int i = 0; i < bulletArray.size(); i++ ){
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            // bullet collision with aliens
            for(int j = 0; j < alienArray.size(); j++){
                Block alien = alienArray.get(j);
                if(!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 50*level; 
                }
            }
        }

        // clear out of screen bullets
        while(bulletArray.size() >0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            bulletArray.remove(0);
        }

        // next level
        if(alienCount == 0){
            // increase aliens
            //score += alineColumn *alienRows *100;  no bonus
            level++;
            alineColumn = Math.min(alineColumn +1, columns/2-2);
            alienRows = Math.min(alienRows+1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 1;
            createAliens();
        }
    }

    public void createAliens(){
        Random random = new Random();
        for (int r = 0; r< alienRows; r++){
            for(int c = 0; c < alineColumn; c++){
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c*alienWidth,
                    alienY + r*alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameOver){
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alineColumn = 3;
            alienRows = 2;
            level = 1;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth){
            ship.x += shipVelocityX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Block bullet  = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }
}
