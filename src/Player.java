import java.util.*;

public class Player {

    public ArrayList<Card> hand;
    public int estimate;
    public int score;
    private int finalScore;
    private boolean isTrumpDecider;
    private String name;


    public Player(ArrayList<Card> hand, int estimate, String name){
        this.hand = hand;
        this.estimate = estimate;
        this.score = 0;
        this.isTrumpDecider = false;
        this.name = name;

    }

    public Player(ArrayList<Card> hand, String name){
        this.hand = hand;
        this.score = 0;
        this.isTrumpDecider = false;
        this.name = name;

    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public void changeHand(ArrayList<Card> hand){
        this.hand = hand;
    }

    public int getEstimate(){
        return estimate;
    }

    public void setEstimate(int e){
        estimate = e;
    }

    public String getName() {
        return name;
    }

    public void incrementScore(){
        score++;
    }

    public int getFinalScore(){
        if (score==estimate){
            if(estimate == 0||isTrumpDecider){
                finalScore*=2;
            }
        }
        return finalScore;
    }

    public int sumSuitInHand(String suit){
        int sum = 0;
        for (Card card : hand){
            if (card.getSuit().equals(suit)){
                sum+=card.getValue();
            }
        }
        return sum;
    }

    public void divide(int startIndex,int endIndex){

        if(startIndex<endIndex && (endIndex-startIndex)>=1){
            int mid = (endIndex + startIndex)/2;
            divide(startIndex, mid);
            divide(mid+1, endIndex);

            merge(startIndex,mid,endIndex);
        }
    }

    public void sortHand(){
        divide(0, this.hand.size()-1);
    }

    public void merge(int startIndex,int midIndex,int endIndex){

        ArrayList<Card> mergedSortedArray = new ArrayList<Card>();

        int leftIndex = startIndex;
        int rightIndex = midIndex+1;

        while(leftIndex<=midIndex && rightIndex<=endIndex){
            if(hand.get(leftIndex).getValue() <= hand.get(rightIndex).getValue()){
                mergedSortedArray.add(hand.get(leftIndex));
                leftIndex++;
            }else{
                mergedSortedArray.add(hand.get(rightIndex));
                rightIndex++;
            }
        }

        //Either of below while loop will execute
        while(leftIndex<=midIndex){
            mergedSortedArray.add(hand.get(leftIndex));
            leftIndex++;
        }

        while(rightIndex<=endIndex){
            mergedSortedArray.add(hand.get(rightIndex));
            rightIndex++;
        }

        int i = 0;
        int j = startIndex;
        //Setting sorted array to original one
        while(i<mergedSortedArray.size()){
            hand.set(j, mergedSortedArray.get(i++));
            j++;
        }
    }


    @Override
    public String toString() {
        String s = "";
        for (Card card:hand){
            s+=card;
        }
        return s;
    }
}
