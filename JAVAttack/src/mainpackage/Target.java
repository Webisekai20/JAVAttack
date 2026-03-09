package mainpackage;

import java.util.Random;

public class Target {
    Define def = new Define();
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
        def.setBossHealth(20 + (def.getLevel() * 2));
    }
}
