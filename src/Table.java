import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Table extends JPanel {
    private ArrayList<Card> deck;
    private Image background = null;
    public User user;
    public Computer computer1;
    public Computer computer2;
    public Computer computer3;
    public Card userOutStandingCard;
    public Card comp1OutStandingCard;
    public Card comp2OutStandingCard;
    public Card comp3OutStandingCard;
    public String currSuit;
    boolean outStanding;
    public Card track1;
    public Card track2;
    public Card track3;
    public Card trackUser;
    public Player winner;
    public boolean userCanPlay;
    public boolean isFinished;


    public Table() {
        try {
            this.background = ImageIO.read(new File("Background/cardtable.jpeg"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }
        deck = new ArrayList<>();
        createDeck();

        final int[] userEstimate = {0};
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '0' || c > '9') {
                } else {
                    userEstimate[0] = Integer.parseInt(String.valueOf(c));
                }
            }
        });

        Player[] players = {user, computer1, computer2, computer3};
        for (int k = 0; k < players.length; k++) {
            ArrayList<Card> hand = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                int ran = (int) (Math.random() * deck.size());
                hand.add(deck.remove(ran));
            }
            int estimate = 0;
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getValue() > 10) {
                    estimate++;
                }
            }

            if (k == 0) {
                user = new User(hand, userEstimate[0], "You");
                user.sortHand();
            }
            if (k == 1) {
                computer1 = new Computer(hand, estimate, "Computer 1");
            }
            if (k == 2) {
                computer2 = new Computer(hand, estimate, "Computer 2");
            }
            if (k == 3) {
                computer3 = new Computer(hand, estimate, "Computer 3");
            }

        }


        Timer timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOutStandingCard == null && comp1OutStandingCard == null && comp2OutStandingCard == null && comp3OutStandingCard == null) {
                    repaint();
                }


            }
        });
        timer.start();
        this.setFocusable(true);

    while (true){
        int res = JOptionPane.showConfirmDialog(null,"READY TO PLAY?","Estimation",JOptionPane.YES_NO_OPTION);
        if (res ==JOptionPane.YES_OPTION){
            break;
        }
    }
        
    }


    public void passEstimate(int e) {
        user.setEstimate(e);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        int increment = 0;
        if (userOutStandingCard != null) {
            this.add(userOutStandingCard.getImage());
            userOutStandingCard.image.setBounds(400, 300, 41, 61);

        }
        if (comp1OutStandingCard != null) {
            this.add(comp1OutStandingCard.getImage());
            comp1OutStandingCard.image.setBounds(600, 200, 41, 61);
        }
        if (comp2OutStandingCard != null) {
            this.add(comp2OutStandingCard.getImage());
            comp2OutStandingCard.image.setBounds(400, 120, 41, 61);
        }
        if (comp3OutStandingCard != null) {
            this.add(comp3OutStandingCard.getImage());
            comp3OutStandingCard.image.setBounds(200, 200, 41, 61);
        }
        for (Card card : user.getHand()) {
            this.add(card.getImage());
            card.image.setBounds(115 + increment, 400, 41, 61);
            card.changeXPos(115 + increment);
            increment += 45;
        }


    }

    public void drawBackground(Graphics2D g2d) {
        if (background != null) g2d.drawImage(background, 0, 0, Game.WIDTH, Game.HEIGHT, null);
    }

    public void showMessage(String s, Graphics2D g2d) {
        Font font = new Font("SansSerif", Font.PLAIN, 40);
        Rectangle2D textBox = font.getStringBounds(s, g2d.getFontRenderContext());
        g2d.setFont(font);
        g2d.setColor(new Color(64, 64, 64, 1000));
        g2d.drawString(s, (int) (Game.WIDTH / 2 - textBox.getWidth() / 2), 50);
    }

    public void createDeck() {
        String temp = null;
        for (int j = 0; j < 4; j++) {
            for (int i = 2; i < 15; i++) {
                if (j == 0) {
                    temp = "s";
                } else if (j == 1) {
                    temp = "d";
                } else if (j == 2) {
                    temp = "c";
                } else {
                    temp = "h";
                }
                deck.add(new Card(i, temp, false));

            }
        }

    }

    public void changeComp1OutStanding(Card e) {


        comp1OutStandingCard = e;
    }

    public void changeComp2OutStanding(Card e) {

        comp2OutStandingCard = e;
    }

    public void changeComp3OutStanding(Card e) {
        comp3OutStandingCard = e;
    }

    public void userStarts() {
        boolean cont = true;
        while (cont) {
            Game.pseudo.setVisible(true);
            String play = JOptionPane.showInputDialog(Game.pseudo, "Play a card (Example: \"sq\" to play the Queen of Spades):");
            Game.pseudo.setVisible(false);
            for (int i = 0; i < user.hand.size(); i++) {
                if (user.hand.get(i).name.equals(play)) {
                    userOutStandingCard = user.hand.remove(i);
                    trackUser = userOutStandingCard;
                    cont = false;
                    break;

                }
            }
        }
        repaint();

        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOutStandingCard != null) {
                    changeComp1OutStanding(computer1.playCard(userOutStandingCard.getSuit(), Game.trump));
                    track1 = comp1OutStandingCard;

                }

                repaint();
            }
        });
        timer1.start();
        timer1.setRepeats(false);

        Timer timer2 = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOutStandingCard != null) {
                    changeComp2OutStanding(computer2.playCard(userOutStandingCard.getSuit(), Game.trump));
                    track2 = comp2OutStandingCard;


                }

                repaint();
            }
        });
        timer2.start();
        timer2.setRepeats(false);


        Timer timer3 = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOutStandingCard != null) {
                    changeComp3OutStanding(computer3.playCard(userOutStandingCard.getSuit(), Game.trump));
                    track3 = comp3OutStandingCard;

                }
                repaint();


            }

        });

        timer3.start();
        timer3.setRepeats(false);


        Timer timer4 = new Timer(8000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userOutStandingCard = null;
                comp1OutStandingCard = null;
                comp2OutStandingCard = null;
                comp3OutStandingCard = null;
                removeAll();
                repaint();




            }
        });

        timer4.setRepeats(false);

        timer4.start();
        while(timer4.isRunning()){}
        Game.trumpDecider= findWinner().getName();




    }


    public void comp1Starts() {
        repaint();

        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (computer1.sumSuitInHand(Game.trump) > 0) {
                    changeComp1OutStanding(computer1.playCard(Game.trump, Game.trump));
                } else {
                    changeComp1OutStanding(computer1.playCard(computer1.hand.get(0).getSuit(), Game.trump));
                }
                track1 = comp1OutStandingCard;


                repaint();
            }
        });
        timer1.setRepeats(false);
        timer1.start();

        Timer timer2 = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp1OutStandingCard != null) {
                    changeComp2OutStanding(computer2.playCard(comp1OutStandingCard.getSuit(), Game.trump));
                    track2 = comp2OutStandingCard;


                }

                repaint();
            }
        });
        timer2.start();
        timer2.setRepeats(false);


        Timer timer3 = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp2OutStandingCard != null) {
                    changeComp3OutStanding(computer3.playCard(comp1OutStandingCard.getSuit(), Game.trump));
                    track3 = comp3OutStandingCard;

                }

                repaint();


            }

        });

        timer3.start();
        timer3.setRepeats(false);

        while (timer3.isRunning()) {
        }

        String play = "";
        if (!timer3.isRunning()) {
            boolean cont = true;
            while (cont) {
                Game.pseudo.setVisible(true);
                play = JOptionPane.showInputDialog(Game.pseudo, "Play a valid card (Example: \"c9\" to play the 9 of Clubs):");
                Game.pseudo.setVisible(false);
                for (int i = 0; i < user.hand.size(); i++) {
                    if (user.sumSuitInHand(track1.getSuit()) > 0) {
                        if (user.hand.get(i).name.equals(play) && user.hand.get(i).getSuit().equals(track1.getSuit())) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                        repaint();
                    }
                    else{
                        if (user.hand.get(i).name.equals(play)) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                    }

                }
                repaint();

            }
        }

        for (int i = 0; i < user.hand.size(); i++) {
            if (user.hand.get(i).name.equals(play)) {
                userOutStandingCard = user.hand.remove(i);
                trackUser = userOutStandingCard;
                repaint();
            }
            repaint();
        }
        repaint();

        Timer timer4 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userOutStandingCard = null;
                comp1OutStandingCard = null;
                comp2OutStandingCard = null;
                comp3OutStandingCard = null;
                removeAll();
                repaint();

            }
        });
        timer4.setRepeats(false);

        timer4.start();
        while(timer4.isRunning()){}
            Game.trumpDecider= findWinner().getName();



    }


    public void comp2Starts() {
        repaint();
        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (computer2.sumSuitInHand(Game.trump) > 0) {
                    changeComp2OutStanding(computer2.playCard(Game.trump, Game.trump));
                    track2 = comp2OutStandingCard;
                    repaint();


                } else {
                    changeComp2OutStanding(computer2.playCard(computer2.hand.get(0).getSuit(), Game.trump));
                    track2 = comp2OutStandingCard;
                    repaint();

                }


                repaint();
            }
        });
        timer1.start();
        timer1.setRepeats(false);

        Timer timer2 = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp2OutStandingCard != null) {
                    changeComp3OutStanding(computer3.playCard(comp2OutStandingCard.getSuit(), Game.trump));
                    track3 = comp3OutStandingCard;
                    repaint();
                    userCanPlay = true;


                }

                repaint();
            }
        });
        timer2.start();
        timer2.setRepeats(false);

        while (timer2.isRunning()) {
        }



        String play = "";
        if (!timer2.isRunning()) {
            boolean cont = true;
            while (cont) {
                Game.pseudo.setVisible(true);
                play = JOptionPane.showInputDialog(Game.pseudo, "Play a valid card (Example: \"da\" to play the Ace of Diamonds):");
                Game.pseudo.setVisible(false);
                for (int i = 0; i < user.hand.size(); i++) {
                    if (user.sumSuitInHand(track2.getSuit()) > 0) {
                        if (user.hand.get(i).name.equals(play) && user.hand.get(i).getSuit().equals(track2.getSuit())) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                        repaint();
                    }
                    else{
                        if (user.hand.get(i).name.equals(play)) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                    }

                }
                repaint();

            }
        }

        for (int i = 0; i < user.hand.size(); i++) {
            if (user.hand.get(i).name.equals(play)) {
                userOutStandingCard = user.hand.remove(i);
                trackUser = userOutStandingCard;
                repaint();
            }
            repaint();
        }

        Timer timer3 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp2OutStandingCard != null) {
                    changeComp1OutStanding(computer1.playCard(comp2OutStandingCard.getSuit(), Game.trump));
                    track1 = comp1OutStandingCard;
                    repaint();

                }

                repaint();


            }

        });

        timer3.start();
        timer3.setRepeats(false);

        Timer timer4 = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userOutStandingCard = null;
                comp1OutStandingCard = null;
                comp2OutStandingCard = null;
                comp3OutStandingCard = null;
                removeAll();
                repaint();

            }
        });
        timer4.setRepeats(false);
        timer4.start();
        while(timer4.isRunning()){}
            Game.trumpDecider= findWinner().getName();



    }

    public void comp3Starts() {
        repaint();
        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (computer3.sumSuitInHand(Game.trump) > 0) {
                    changeComp3OutStanding(computer3.playCard(Game.trump, Game.trump));
                    track3 = comp3OutStandingCard;
                    repaint();

                } else {
                    changeComp3OutStanding(computer3.playCard(computer3.hand.get(0).getSuit(), Game.trump));
                    track3 = comp3OutStandingCard;
                    repaint();

                }
                userCanPlay = true;

                repaint();
            }
        });
        timer1.start();
        timer1.setRepeats(false);

        while (timer1.isRunning()) {
        }
        repaint();


        String play = "";
        if (!timer1.isRunning()) {
            boolean cont = true;
            while (cont) {
                Game.pseudo.setVisible(true);
                play = JOptionPane.showInputDialog(Game.pseudo, "Play a valid card (Example: \"h8\" to play the 8 of Hearts):");
                Game.pseudo.setVisible(false);
                for (int i = 0; i < user.hand.size(); i++) {
                    if (user.sumSuitInHand(track3.getSuit()) > 0) {
                        if (user.hand.get(i).name.equals(play) && user.hand.get(i).getSuit().equals(track3.getSuit())) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                        repaint();
                    }
                    else{
                        if (user.hand.get(i).name.equals(play)) {
                            userOutStandingCard = user.hand.remove(i);
                            trackUser = userOutStandingCard;
                            repaint();
                            cont = false;
                            break;
                        }
                    }

                }
                repaint();

            }
        }
        repaint();
        for (int i = 0; i < user.hand.size(); i++) {
            if (user.hand.get(i).name.equals(play)) {
                userOutStandingCard = user.hand.remove(i);
                trackUser = userOutStandingCard;
                repaint();
            }
            repaint();
        }

        Timer timer3 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp3OutStandingCard != null) {
                    changeComp1OutStanding(computer1.playCard(comp3OutStandingCard.getSuit(), Game.trump));
                    track1 = comp1OutStandingCard;
                    repaint();


                }

                repaint();


            }

        });

        timer3.start();
        timer3.setRepeats(false);

        Timer timer2 = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp3OutStandingCard != null) {
                    changeComp2OutStanding(computer2.playCard(comp3OutStandingCard.getSuit(), Game.trump));
                    track2 = comp2OutStandingCard;
                    repaint();


                }

                repaint();
            }
        });
        timer2.start();
        timer2.setRepeats(false);


        Timer timer4 = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userOutStandingCard = null;
                comp1OutStandingCard = null;
                comp2OutStandingCard = null;
                comp3OutStandingCard = null;
                removeAll();
                repaint();


            }
        });
        timer4.setRepeats(false);
        timer4.start();
        while(timer4.isRunning()){}
        Game.trumpDecider= findWinner().getName();
        if (Game.trumpDecider.equals("You")){
            user.score++;
        }
        else if (Game.trumpDecider.equals("Computer 1")){
            computer1.score++;
        }
        else if (Game.trumpDecider.equals("Computer 2")){
            computer2.score++;
        }
        else{
            computer3.score++;
        }



    }


    public Card findHighestCard(Card c1, Card c2, Card c3) {
        if (c1.getValue() > c2.getValue() && c1.getValue() > c3.getValue()) {
            return c1;
        } else if (c2.getValue() > c3.getValue()) {
            return c2;
        } else {
            return c3;
        }
    }

    public Card findHighestCard(Card c1, Card c2, Card c3, Card c4) {
        if (c1.getValue() > c2.getValue() && c1.getValue() > c3.getValue() && c1.getValue() > c4.getValue()) {
            return c1;
        } else if (c2.getValue() > c3.getValue() && c2.getValue() > c4.getValue()) {
            return c2;
        } else if (c3.getValue() > c4.getValue()) {
            return c3;
        } else {
            return c4;
        }
    }

    public Card findHighestCard(Card c1, Card c2) {
        if (c1.getValue() > c2.getValue()) {
            return c1;
        } else {
            return c2;
        }
    }

    public Player findWinner() {

        if ((trackUser.isTrump() && track1.isTrump() && track2.isTrump() && track3.isTrump())) {
            Card winner = findHighestCard(trackUser, track1, track2, track3);
            if (winner.equals(trackUser)) {
                return user;
            }
            if (winner.equals(track1)) {
                return computer1;
            }
            if (winner.equals(track2)) {
                return computer2;
            }
            if (winner.equals(track3)) {
                return computer3;
            }
        } else if (track1.isTrump() && track2.isTrump() && track3.isTrump()) {
            Card winner = findHighestCard(track1, track2, track3);
            if (winner.equals(track1)) {
                return computer1;
            }
            if (winner.equals(track2)) {
                return computer2;
            }
            if (winner.equals(track3)) {
                return computer3;
            }
        } else if (trackUser.isTrump() && track1.isTrump() && track2.isTrump()) {
            Card winner = findHighestCard(track1, track2, trackUser);
            if (winner.equals(track1)) {
                return computer1;
            }
            if (winner.equals(track2)) {
                return computer2;
            }
            if (winner.equals(trackUser)) {
                return user;
            }
        } else if (track3.isTrump() && track1.isTrump() && trackUser.isTrump()) {
            Card winner = findHighestCard(track1, trackUser, track3);
            if (winner.equals(track1)) {
                return computer1;
            }
            if (winner.equals(trackUser)) {
                return user;
            }
            if (winner.equals(track3)) {
                return computer3;
            }
        } else if (track3.isTrump() && track2.isTrump() && trackUser.isTrump()) {
            Card winner = findHighestCard(trackUser, track2, track3);
            if (winner.equals(trackUser)) {
                return user;
            }
            if (winner.equals(track2)) {
                return computer2;
            }
            if (winner.equals(track3)) {
                return computer3;
            }
        } else if (track3.isTrump() && track2.isTrump()) {
            Card winner = findHighestCard(track3, track2);
            if (winner.equals(track3)) {
                return computer3;
            }
            if (winner.equals(track2)) {
                return computer2;
            }

        } else if (track3.isTrump() && track1.isTrump()) {
            Card winner = findHighestCard(track3, track1);
            if (winner.equals(track3)) {
                return computer3;
            }
            if (winner.equals(track1)) {
                return computer1;
            }

        } else if (track3.isTrump() && trackUser.isTrump()) {
            Card winner = findHighestCard(track3, trackUser);
            if (winner.equals(track3)) {
                return computer3;
            }
            if (winner.equals(trackUser)) {
                return user;
            }
        } else if (track2.isTrump() && trackUser.isTrump()) {
            Card winner = findHighestCard(track2, trackUser);
            if (winner.equals(track2)) {
                return computer2;
            }
            return user;

        } else if (track2.isTrump() && track1.isTrump()) {
            Card winner = findHighestCard(track2, track1);
            if (winner.equals(track2)) {
                return computer2;
            }
            return computer1;
        } else if (track1.isTrump() && trackUser.isTrump()) {
            Card winner = findHighestCard(track1, trackUser);
            if (winner.equals(track1)) {
                return computer1;
            }
            return user;
        } else if (track1.isTrump()) {
            return computer1;

        } else if (track2.isTrump()) {
            return computer2;

        } else if (track3.isTrump()) {
            return computer3;

        } else if (trackUser.isTrump()) {
            return user;
        } else {
            Card winner = null;
            if (Game.trumpDecider.equals("You")){
                if(!track1.getSuit().equals(trackUser.getSuit())&&!track2.getSuit().equals(trackUser.getSuit())&&!track3.getSuit().equals(trackUser.getSuit())) {
                    return user;
                }
                else if(!track1.getSuit().equals(trackUser.getSuit())&&!track2.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!track1.getSuit().equals(trackUser.getSuit())&&!track3.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    return user;
                }
                else if(!track3.getSuit().equals(trackUser.getSuit())&&!track2.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track1);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    return user;
                }
                else if(!track3.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track1,track2);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track2)){
                        return computer2;
                    }
                    return user;
                }
                else if(!track2.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track3,track1);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!track1.getSuit().equals(trackUser.getSuit())){
                    winner=findHighestCard(trackUser, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else{
                    winner = findHighestCard(trackUser, track1, track2, track3);
                    if (winner.equals(trackUser)) {
                        return user;
                    }
                    if (winner.equals(track1)) {
                        return computer1;
                    }
                    if (winner.equals(track2)) {
                        return computer2;
                    }
                    if (winner.equals(track3)) {
                        return computer3;
                    }
                }
            }

            else if (Game.trumpDecider.equals("Computer 1")){
                if(!trackUser.getSuit().equals(track1.getSuit())&&!track2.getSuit().equals(track1.getSuit())&&!track3.getSuit().equals(track1.getSuit())) {
                    return computer1;
                }
                else if(!trackUser.getSuit().equals(track1.getSuit())&&!track2.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(track1, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return computer1;
                }
                else if(!trackUser.getSuit().equals(track1.getSuit())&&!track3.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(track1, track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    return computer1;
                }
                else if(!track3.getSuit().equals(track1.getSuit())&&!track2.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(trackUser, track1);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    return user;
                }
                else if(!track3.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(trackUser, track1,track2);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track2)){
                        return computer2;
                    }
                    return user;
                }
                else if(!track2.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(trackUser, track3,track1);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!trackUser.getSuit().equals(track1.getSuit())){
                    winner=findHighestCard(track1, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return computer1;
                }
                else{
                    winner = findHighestCard(trackUser, track1, track2, track3);
                    if (winner.equals(trackUser)) {
                        return user;
                    }
                    if (winner.equals(track1)) {
                        return computer1;
                    }
                    if (winner.equals(track2)) {
                        return computer2;
                    }
                    if (winner.equals(track3)) {
                        return computer3;
                    }
                }
            }
            else if (Game.trumpDecider.equals("Computer 2")){
                if(!trackUser.getSuit().equals(track2.getSuit())&&!track1.getSuit().equals(track2.getSuit())&&!track3.getSuit().equals(track2.getSuit())) {
                    return computer2;
                }
                else if(!trackUser.getSuit().equals(track2.getSuit())&&!track1.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(track2, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return computer2;
                }
                else if(!trackUser.getSuit().equals(track2.getSuit())&&!track3.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(track1, track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    return computer1;
                }
                else if(!track3.getSuit().equals(track2.getSuit())&&!track1.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(trackUser, track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    return user;
                }
                else if(!track3.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(trackUser, track1,track2);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track2)){
                        return computer2;
                    }
                    return user;
                }
                else if(!track1.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(trackUser, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!trackUser.getSuit().equals(track2.getSuit())){
                    winner=findHighestCard(track1, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return computer1;
                }
                else{
                    winner = findHighestCard(trackUser, track1, track2, track3);
                    if (winner.equals(trackUser)) {
                        return user;
                    }
                    if (winner.equals(track1)) {
                        return computer1;
                    }
                    if (winner.equals(track2)) {
                        return computer2;
                    }
                    if (winner.equals(track3)) {
                        return computer3;
                    }
                }
            }

            else{
                if(!trackUser.getSuit().equals(track3.getSuit())&&!track1.getSuit().equals(track3.getSuit())&&!track2.getSuit().equals(track3.getSuit())) {
                    return computer3;
                }
                else if(!trackUser.getSuit().equals(track3.getSuit())&&!track1.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(track2, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return computer2;
                }
                else if(!trackUser.getSuit().equals(track3.getSuit())&&!track2.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(track1, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return computer1;
                }
                else if(!track2.getSuit().equals(track3.getSuit())&&!track1.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(trackUser, track3);
                    if (winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!track2.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(trackUser, track1,track3);
                    if (winner.equals(track1)){
                        return computer1;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!track1.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(trackUser, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return user;
                }
                else if(!trackUser.getSuit().equals(track3.getSuit())){
                    winner=findHighestCard(track1, track3,track2);
                    if (winner.equals(track2)){
                        return computer2;
                    }
                    if(winner.equals(track3)){
                        return computer3;
                    }
                    return computer1;
                }
                else{
                    winner = findHighestCard(trackUser, track1, track2, track3);
                    if (winner.equals(trackUser)) {
                        return user;
                    }
                    if (winner.equals(track1)) {
                        return computer1;
                    }
                    if (winner.equals(track2)) {
                        return computer2;
                    }
                    if (winner.equals(track3)) {
                        return computer3;
                    }
                }
            }
        }
        return null;

    }


}