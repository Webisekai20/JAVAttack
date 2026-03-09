package mainpackage;


import java.util.ArrayList;


import javax.sound.sampled.Clip;

public class Define {
    //board
    private final int tileSize = 32;
    private final int rows = 16;
    private final int columns = 16;
    private final int boardWidth = tileSize * columns;
    private final int boardHeight = tileSize * rows;
    public int getTileSize() { return tileSize; }
    public int getRow() { return rows; }
    public int getColumn() {return columns;}
    public int getBoardWidth() {return boardWidth;}
    public int getBoardHeight() {return boardHeight;}

    final int powerWidth = tileSize *2;
    final int powerHeight = tileSize;
    public int getPowerHeight() { return powerHeight;}
    public int getPowerWidth() {return powerWidth;}

      // bullets
    ArrayList<Block> bulletArray;
    ArrayList<Block> alienBullets;
    final int bulletWidth = tileSize/8;
    final int bulletHeight = tileSize/2;
    final int bulletVelocityY = -7;// moving speed  
    public int getBulletWidth() {return bulletWidth;}
    public int getBulletHeight() {return bulletHeight;}
    public int getBulletVelocityY() {return bulletVelocityY;}

    // powerups
    int powerVelocity = 0; // speed boost | 0
    int attackSize = 0; // attack size | 1
    int offset = 0;
    int scoreBoost = 1; // multiplier | 2
    int bufferTimeBoost = 0; // attack speed boost | 3
    public int getPowerVelocity() { return powerVelocity;}
    public int getAttackSize() { return attackSize;}
    public int getOffset() { return offset;}
    public int getScoreBoost() { return scoreBoost;}
    public int getBufferTimeBoost() { return bufferTimeBoost;}

    public void setPowerVelocity(int powerVelocity) { this.powerVelocity = powerVelocity; }
    public void setAttackSize(int attackSize) { this.attackSize = attackSize; }
    public void setOffset(int offset) { this.offset = offset; }
    public void setScoreBoost(int scoreBoost) { this.scoreBoost = scoreBoost; }
    public void setBufferTimeBoost(int bufferTimeBoost) { this.bufferTimeBoost = bufferTimeBoost; }

     //shooting mechanics
    long shootBuffer = 0;
    long bufferTime = 500 - bufferTimeBoost*2; //in milliseconds
    public long getShootBuffer() { return shootBuffer; }
    public long getBufferTime() { return bufferTime; }
    public void setShootBuffer(long shootBuffer) { this.shootBuffer = shootBuffer; }
    public void setBufferTime(long bufferTime) { this.bufferTime = bufferTime; }


    //movement booleans
    boolean left = false;
    boolean right = false;
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    
    //ship
    int shipWidth = tileSize;  // 64
    int shipHeight = tileSize/2;  ///32
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = tileSize/5;
    public int getShipWidth() { return shipWidth; }
    public int getShipHeight() { return shipHeight; }
    public int getShipX() { return shipX; }
    public int getShipY() { return shipY; }
    public int getShipVelocityX() { return shipVelocityX; }

    
    //aliens
    final int alienWidth = tileSize;
    final int alienHeight = tileSize/2;
    final int alienX = tileSize;
    final int alienY = tileSize;
    public int getAlienWidth() {return alienWidth;}
    public int getAlienHeight() {return alienHeight;}
    public int getAlienX() {return alienX;}
    public int getAlienY() {return alienY;}


     int alienRows = 2;
     int alienColumn = 3;
     int alienCount = 0; // num of aliens to defeat
     int alienVelocityX = 4; //1;
     int blinkFactor;
    public int getAlienRows() {return alienRows;}
    public int getAlienColumn() {return alienColumn;}
    public int getAlienCount() {return alienCount;}
    public int getAlienVelocityX() {return alienVelocityX;}
    public void setAlienRows(int alienRows) {this.alienRows = alienRows;}
    public void setAlienColumn(int alienColumn) {this.alienColumn = alienColumn;}
    public void setAlienCount(int alienCount) {this.alienCount = alienCount;}
    public void setAlienVelocityX(int alienVelocityX) {this.alienVelocityX = alienVelocityX;}
    
        // boss
    Block boss = null;
    boolean isBossAlive = false;
    final int bossWidth = tileSize * 3;
    final int bossHeight = tileSize;
    int bossHealth = 20;
    int bossVelocityX = 2;
    public Block getBoss() {return boss;}
    public boolean isBossAlive() {return isBossAlive;}
    public int getBossWidth() {return bossWidth;}
    public int getBossHeight() {return bossHeight;}
    public int getBossHealth() {return bossHealth;}
    public int getBossVelocityX() {return bossVelocityX;}
    public void setBoss(Block boss) {this.boss = boss;}
    public void setBossAlive(boolean isBossAlive) {this.isBossAlive = isBossAlive;}
    public void setBossHealth(int bossHealth) {this.bossHealth = bossHealth;}
    public void setBossVelocityX(int bossVelocityX) {this.bossVelocityX = bossVelocityX;}

    private boolean paused = false;
    public boolean isPaused() {return paused;}
    public void setPaused(boolean paused) {this.paused = paused;}
    

    // game Exe
    private String playerName = "AL";
    private int score = 0;
    private int level = 1;
    private long pressBuffer = 0;
    private long blinkBUffer = 850;
    private long lastBlinkBUffer = 0;
    public String getPlayerName() {return playerName;}
    public int getScore() {return score;}
    public int getLevel() {return level;}
    public long getPressBuffer() {return pressBuffer;}
    public long getBlinkBuffer() {return blinkBUffer;}
    public long getLastBlinkBuffer() {return lastBlinkBUffer;}
    public void setLevel(int level) {this.level = level;}
    public void setScore(int score) {this.score = score;}
    public void setPressBuffer(long pressBuffer) {this.pressBuffer = pressBuffer;}
    public void setLastBlinkBuffer(long lastBlinkBuffer) {this.lastBlinkBUffer = lastBlinkBuffer;}

}
