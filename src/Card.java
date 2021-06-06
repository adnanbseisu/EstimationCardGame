import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card extends JPanel {

    private int value;
    private String suit;
    private boolean isTrump;
    public JLabel image;
    private boolean cardPlayed;
    public int xPos, yPos;
    public String name;


    public Card(int value, String suit, boolean isTrump) {
        this.value = value;
        this.suit = suit;
        this.isTrump = isTrump;
        String fileName = suit+"."+value+".gif";
        if (value == 11){
            name = suit+"j";
        }
        else if(value == 12){
            name = suit+"q";
        }
        else if (value == 13){
            name = suit +"k";
        }
        else if(value == 14){
            name= suit + "a";
        }
        else{name = suit+value;}
        ImageIcon imgIcon = new ImageIcon(this.getClass().getResource(fileName));
        image = new JLabel(imgIcon);
        yPos = 400;
       // image.setBounds(100, 100, 100, 100); // for example, you can use your own values



    }

    public String getSuit() {
        return suit;
    }

    public void changeXPos(int newXPos){
        xPos = newXPos;
    }
    public void changeYPos(int newYPos){
        yPos = newYPos;
    }

    public JLabel getImage(){
        return image;
    }

    public int getValue(){
        return value;
    }

    public void drawCard(Graphics2D g2d){
        super.paintComponent(g2d);
    }

    public void makeTrump(){
        isTrump = true;
    }

    public boolean isTrump(){
        return isTrump;
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", suit='" + suit + '\'' +
                ", isTrump=" + isTrump +
                '}';
    }

    /*public void paintCard(Graphics2D g2d, Card card, Rectangle bounds) {
        g2d.translate(bounds.x + 5, bounds.y + 5);
        g2d.setClip(0, 0, bounds.width - 5, bounds.height - 5);

        String text = card.getValue() + card.getSuit();
        FontMetrics fm = g2d.getFontMetrics();

        g2d.drawString(text, 0, fm.getAscent());
    }

     */
}
