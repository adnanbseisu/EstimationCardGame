import java.util.ArrayList;

public class Computer extends Player{
    public Computer(ArrayList<Card> hand, int estimate, String name) {
        super(hand, estimate, name);
    }

    public Card playCard(String suit,String trump) {
        int suitCount = 0;
        for(Card card: hand) {
            if (card.getSuit().equals(suit)) {
                suitCount++;
            }
        }
        if (score<getEstimate()){
            if(suitCount!=0){
                for(int i=hand.size()-1;i>=0;i--){
                    if(hand.get(i).getSuit().equals(suit)){
                        return hand.remove(i);
                    }
                }
            }
            else{
                for(int i=hand.size()-1;i>=0;i--){
                    if(hand.get(i).getSuit().equals(trump)){
                        return hand.remove(i);
                    }
                }
                return hand.remove(0);
            }
        }
        else{
            if(suitCount!=0){
                for(int i=0;i<hand.size();i++){
                    if(hand.get(i).getSuit().equals(suit)){
                        return hand.remove(i);
                    }
                }
            }
            else{
                return(hand.remove(0));
            }
        }
        return null;
    }
}
