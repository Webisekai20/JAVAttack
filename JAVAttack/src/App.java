import java.util.jar.JarEntry;

import javax.swing.*;



public class App {
    public static void main(String[] args) throws Exception {
        final int tileSize  =32;
        final int rows = 16;
        final int columns = 16;
        final int boardWidth = tileSize * columns;
        final int boardHeight = tileSize * rows;
       

        JFrame frame = new JFrame("JAVAttack");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JAVAttack javAttack = new JAVAttack();
        frame.add(javAttack);
        frame.pack();
        javAttack.requestFocus();
        frame.setVisible(true);
    }
}
