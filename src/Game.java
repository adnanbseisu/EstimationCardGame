import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class Game extends JFrame {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;
    public static Table t;
    public static String trump;
    public static String trumpDecider = "";
    public static int round = 0;
    public static boolean play;
    public static String trumpMessage = "";
    public static JFrame pseudo;
    public static JFrame window;
    public static JFrame winningScreen;

    public Game() {
        t = new Table();
        t.setVisible(true);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.add(t);
        pseudo = new JFrame();
        pseudo.setSize(0, 0);
        pseudo.setVisible(false);
        pseudo.setLocation(900,500);


    }


    public static void main(String[] args) {
        run();
    }


    public static void run() {
        Game game = new Game();
        game.setVisible(true);
        Game.prepGame();

            for (int i = 0; i < 13; i++) {
                Game.playGame();
            }
            pseudo.setVisible(false);
            window.setVisible(false);
            game.setVisible(false);
            String winScreen = "";
            if (t.user.score == t.user.estimate) {
                winScreen += t.user.getName() + "-";
            }
            if (t.computer1.score == t.computer1.estimate) {
                winScreen += t.computer1.getName()+ "-";
            }
            if (t.computer2.score == t.computer2.estimate) {
                winScreen += t.computer2.getName()+ "-";
            }
            if (t.computer3.score == t.computer3.estimate) {
                winScreen += t.computer3.getName()+ "-";
            }

            if (winScreen.equals("")){
                winScreen = "No One :( - ";
            }

        winScreen = "GAME OVER! Here's who estimated correctly: " + winScreen +"CLICK TO PLAY AGAIN!";

        winningScreen = new JFrame();
        winningScreen.setSize(2000, 1500);
        winningScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel info = new JLabel(winScreen,SwingConstants.CENTER);
        info.setFont(new Font("Times", Font.PLAIN, 30));
        winningScreen.add(info);
        winningScreen.setVisible(true);
        if (winningScreen.isVisible()){
            winningScreen.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    window.setVisible(false);
                    game.setVisible(false);
                    winningScreen.setVisible(false);
                    run();

                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

            });
        }
        }

    public static void prepGame() {
        while (true){
            String estimateString = JOptionPane.showInputDialog(t,"Estimate:");
            try {
                t.user.setEstimate(Integer.parseInt(estimateString));
                break;
            }
            catch (Exception e){
            }
        }
        if (t.user.getEstimate() >= t.computer1.getEstimate() && t.user.getEstimate() >= t.computer2.getEstimate() && t.user.getEstimate() >= t.computer3.getEstimate()) {
            trumpDecider = "You";
            while (true){
                trump = JOptionPane.showInputDialog(t,"Your estimate was highest. Enter a Trump (\"d\"/\"c\"/\"s\"/\"h\")");
                trump.toLowerCase(Locale.ROOT);
                if (trump.equals("d")||trump.equals("h")||trump.equals("c")||trump.equals("d")){
                    break;
                }
            }
            //Computer 1
            int tempEstimate = 0;
            if (t.computer1.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer1.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer1.getEstimate() / 2 + tempEstimate;
            t.computer1.setEstimate(tempEstimate);
            //Computer 2
            tempEstimate = 0;
            if (t.computer2.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer2.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer2.getEstimate() / 2 + tempEstimate;
            t.computer2.setEstimate(tempEstimate);
            //Computer 3
            tempEstimate = 0;
            if (t.computer3.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer3.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer3.getEstimate() / 2 + tempEstimate;
            t.computer3.setEstimate(tempEstimate);

            String trumpMessage = "";
            if (trump.equals("s")) {
                trumpMessage = "spades";
            }
            if (trump.equals("d")) {
                trumpMessage = "diamonds";
            }
            if (trump.equals("c")) {
                trumpMessage = "clubs";
            }
            if (trump.equals("h")) {
                trumpMessage = "hearts";
            }
            JOptionPane.showMessageDialog(t, trumpDecider + " won the bidding round. " + trumpDecider + " chose " + trumpMessage + " as the trump suit.");


        } else if (t.computer1.getEstimate() >= t.computer2.getEstimate() && t.computer2.getEstimate() >= t.computer3.getEstimate()) {
            trumpDecider = "Computer 1";
            int spadesCount = 0;
            int clubsCount = 0;
            int heartsCount = 0;
            int diamondsCount = 0;
            for (Card card : t.computer1.hand) {
                if (card.getSuit().equals("s")) {
                    spadesCount++;
                } else if (card.getSuit().equals("c")) {
                    clubsCount++;
                } else if (card.getSuit().equals("h")) {
                    heartsCount++;
                } else {
                    diamondsCount++;
                }

            }


            if (spadesCount > clubsCount && spadesCount > heartsCount && spadesCount > diamondsCount) {
                trump = "s";
            } else if (clubsCount > spadesCount && clubsCount > heartsCount && clubsCount > diamondsCount) {
                trump = "c";
            } else if (diamondsCount > spadesCount && diamondsCount > heartsCount && diamondsCount > clubsCount) {
                trump = "d";
            } else if (heartsCount > spadesCount && heartsCount > diamondsCount && heartsCount > clubsCount) {
                trump = "h";
            } else {
                if (t.computer1.sumSuitInHand("s") >= t.computer1.sumSuitInHand("c") && t.computer1.sumSuitInHand("s") >= t.computer1.sumSuitInHand("d") && t.computer1.sumSuitInHand("s") >= t.computer1.sumSuitInHand("h")) {
                    trump = "s";
                } else if (t.computer1.sumSuitInHand("c") >= t.computer1.sumSuitInHand("h") && t.computer1.sumSuitInHand("c") >= t.computer1.sumSuitInHand("d")) {
                    trump = "c";
                } else if (t.computer1.sumSuitInHand("d") >= t.computer1.sumSuitInHand("h")) {
                    trump = "d";
                } else {
                    trump = "h";
                }
            }

            String trumpMessage = "";
            if (trump.equals("s")) {
                trumpMessage = "spades";
            }
            if (trump.equals("d")) {
                trumpMessage = "diamonds";
            }
            if (trump.equals("c")) {
                trumpMessage = "clubs";
            }
            if (trump.equals("h")) {
                trumpMessage = "hearts";
            }
            JOptionPane.showMessageDialog(t, trumpDecider + " won the bidding round. " + trumpDecider + " chose " + trumpMessage + " as the trump suit.");

            //User
            int tempEstimate;
            while (true){
                try{
                    String tempTempEstimate = JOptionPane.showInputDialog(t,"Now that you know that the trump suit is " + trumpMessage + ", what is your new estimate?");
                    tempEstimate = Integer.parseInt(tempTempEstimate);
                    break;
                }
                catch (Exception e){

                }
            }
            t.user.setEstimate(tempEstimate);
            //Computer 2
            tempEstimate = 0;
            if (t.computer2.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer2.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer2.getEstimate() / 2 + tempEstimate;
            t.computer2.setEstimate(tempEstimate);
            //Computer 3
            tempEstimate = 0;
            if (t.computer3.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer3.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer3.getEstimate() / 2 + tempEstimate;
            t.computer3.setEstimate(tempEstimate);


        } else if (t.computer2.getEstimate() >= t.computer3.getEstimate()) {
            trumpDecider = "Computer 2";
            int spadesCount = 0;
            int clubsCount = 0;
            int heartsCount = 0;
            int diamondsCount = 0;
            for (Card card : t.computer2.hand) {
                if (card.getSuit().equals("s")) {
                    spadesCount++;
                } else if (card.getSuit().equals("c")) {
                    clubsCount++;
                } else if (card.getSuit().equals("h")) {
                    heartsCount++;
                } else {
                    diamondsCount++;
                }

            }
            if (spadesCount > clubsCount && spadesCount > heartsCount && spadesCount > diamondsCount) {
                trump = "s";
            } else if (clubsCount > spadesCount && clubsCount > heartsCount && clubsCount > diamondsCount) {
                trump = "c";
            } else if (diamondsCount > spadesCount && diamondsCount > heartsCount && diamondsCount > clubsCount) {
                trump = "d";
            } else if (heartsCount > spadesCount && heartsCount > diamondsCount && heartsCount > clubsCount) {
                trump = "h";
            } else {
                if (t.computer2.sumSuitInHand("s") >= t.computer2.sumSuitInHand("c") && t.computer2.sumSuitInHand("s") >= t.computer2.sumSuitInHand("d") && t.computer2.sumSuitInHand("s") >= t.computer2.sumSuitInHand("h")) {
                    trump = "s";
                } else if (t.computer2.sumSuitInHand("c") >= t.computer2.sumSuitInHand("h") && t.computer2.sumSuitInHand("c") >= t.computer2.sumSuitInHand("d")) {
                    trump = "c";
                } else if (t.computer2.sumSuitInHand("d") >= t.computer2.sumSuitInHand("h")) {
                    trump = "d";
                } else {
                    trump = "h";
                }
            }

            String trumpMessage = "";
            if (trump.equals("s")) {
                trumpMessage = "spades";
            }
            if (trump.equals("d")) {
                trumpMessage = "diamonds";
            }
            if (trump.equals("c")) {
                trumpMessage = "clubs";
            }
            if (trump.equals("h")) {
                trumpMessage = "hearts";
            }
            JOptionPane.showMessageDialog(t, trumpDecider + " won the bidding round. " + trumpDecider + " chose " + trumpMessage + " as the trump suit.");

            //User
            String tempTempEstimate = JOptionPane.showInputDialog(t,"Now that you know that the trump suit is " + trumpMessage + ", what is your new estimate?");
            int tempEstimate = Integer.parseInt(tempTempEstimate);
            t.user.setEstimate(tempEstimate);
            //Computer 1
            tempEstimate = 0;
            if (t.computer1.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer1.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer1.getEstimate() / 2 + tempEstimate;
            t.computer1.setEstimate(tempEstimate);
            //Computer 3
            tempEstimate = 0;
            if (t.computer3.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer3.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer3.getEstimate() / 2 + tempEstimate;
            t.computer3.setEstimate(tempEstimate);
        } else {
            trumpDecider = "Computer 3";
            int spadesCount = 0;
            int clubsCount = 0;
            int heartsCount = 0;
            int diamondsCount = 0;
            for (Card card : t.computer3.hand) {
                if (card.getSuit().equals("s")) {
                    spadesCount++;
                } else if (card.getSuit().equals("c")) {
                    clubsCount++;
                } else if (card.getSuit().equals("h")) {
                    heartsCount++;
                } else {
                    diamondsCount++;
                }

            }
            if (spadesCount > clubsCount && spadesCount > heartsCount && spadesCount > diamondsCount) {
                trump = "s";
            } else if (clubsCount > spadesCount && clubsCount > heartsCount && clubsCount > diamondsCount) {
                trump = "c";
            } else if (diamondsCount > spadesCount && diamondsCount > heartsCount && diamondsCount > clubsCount) {
                trump = "d";
            } else if (heartsCount > spadesCount && heartsCount > diamondsCount && heartsCount > clubsCount) {
                trump = "h";
            } else {
                if (t.computer3.sumSuitInHand("s") >= t.computer3.sumSuitInHand("c") && t.computer3.sumSuitInHand("s") >= t.computer3.sumSuitInHand("d") && t.computer3.sumSuitInHand("s") >= t.computer3.sumSuitInHand("h")) {
                    trump = "s";
                } else if (t.computer3.sumSuitInHand("c") >= t.computer3.sumSuitInHand("h") && t.computer3.sumSuitInHand("c") >= t.computer3.sumSuitInHand("d")) {
                    trump = "c";
                } else if (t.computer3.sumSuitInHand("d") >= t.computer3.sumSuitInHand("h")) {
                    trump = "d";
                } else {
                    trump = "h";
                }
            }


            if (trump.equals("s")) {
                trumpMessage = "spades";
            }
            if (trump.equals("d")) {
                trumpMessage = "diamonds";
            }
            if (trump.equals("c")) {
                trumpMessage = "clubs";
            }
            if (trump.equals("h")) {
                trumpMessage = "hearts";
            }
            JOptionPane.showMessageDialog(t, trumpDecider + " won the bidding round. " + trumpDecider + " chose " + trumpMessage + " as the trump suit.");


            //User
            String tempTempEstimate = JOptionPane.showInputDialog(t,"Now that you know that the trump suit is " + trumpMessage + ", what is your new estimate?");
            int tempEstimate = Integer.parseInt(tempTempEstimate);
            t.user.setEstimate(tempEstimate);
            //Computer 2
            tempEstimate = 0;
            if (t.computer2.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer2.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer2.getEstimate() / 2 + tempEstimate;
            t.computer2.setEstimate(tempEstimate);
            //Computer 1
            tempEstimate = 0;
            if (t.computer1.sumSuitInHand(trump) > 15) {
                tempEstimate++;
            }
            if (t.computer1.sumSuitInHand(trump) > 11) {
                tempEstimate++;
            }
            tempEstimate = t.computer1.getEstimate() / 2 + tempEstimate;
            t.computer1.setEstimate(tempEstimate);
        }

        for (int i = 0; i < t.computer1.hand.size(); i++) {
            if (t.computer1.hand.get(i).getSuit().equals(trump)) {
                t.computer1.hand.set(i, new Card(t.computer1.hand.get(i).getValue(), t.computer1.hand.get(i).getSuit(), true));
            }
        }
        for (int i = 0; i < t.computer2.hand.size(); i++) {
            if (t.computer2.hand.get(i).getSuit().equals(trump)) {
                t.computer2.hand.set(i, new Card(t.computer2.hand.get(i).getValue(), t.computer2.hand.get(i).getSuit(), true));
            }
        }
        for (int i = 0; i < t.computer3.hand.size(); i++) {
            if (t.computer3.hand.get(i).getSuit().equals(trump)) {
                t.computer3.hand.set(i, new Card(t.computer3.hand.get(i).getValue(), t.computer3.hand.get(i).getSuit(), true));
            }
        }
        for (int i = 0; i < t.user.hand.size(); i++) {
            if (t.user.hand.get(i).getSuit().equals(trump)) {
                t.user.hand.set(i, new Card(t.user.hand.get(i).getValue(), t.user.hand.get(i).getSuit(), true));
            }
        }

        window = new JFrame("Trump is "+ trumpMessage);
        window.setSize(280, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String show = "\nYour Estimate: " + t.user.getEstimate() + "\nComp 1 Estimate: " + t.computer1.getEstimate() + "\nComp 2 Estimate: " + t.computer2.getEstimate() + "\nComp 3 Estimate: " + t.computer3.getEstimate();
        JLabel info = new JLabel();
        info.setFont(new Font("Serif", Font.PLAIN, 30));
        info.setText("<html>" + show + "</html>");
        window.add(info);
        window.setVisible(true);
        window.setLocation((int)(t.getLocation().getX()+800),(int)(t.getLocation().getY()));

    }

    public static void playGame() {
        if (trumpDecider.equals("You")) {
            t.userStarts();


        } else if (trumpDecider.equals("Computer 1")) {
            t.comp1Starts();


        } else if (trumpDecider.equals("Computer 2")) {
            t.comp2Starts();


        } else if (trumpDecider.equals("Computer 3")) {
            t.comp3Starts();


        } else {
        }
    }


    public static void playMessage() {
        //JOptionPane.showMessageDialog(t, "Click a card to play it");
    }
}