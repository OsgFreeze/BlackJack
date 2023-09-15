package master;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class stack {

    Scanner input = new Scanner(System.in);
    double[] playerCards = new double[10];
    double[] dealerCards = new double[10];
    double playerVal;
    double dealerVal;

    public void dealStartHand(){

        // Player Starthand
        System.out.println("Du hast 2 Karten bekommen: ");
        generateCards(2, 0, 1);
        playerVal = getVal(playerCards);
        System.out.println("Your Value: " + playerVal);

        // Dealer Starthand
        System.out.println("Der Dealer hat: ");
        generateCards(2, 0, 2);
        dealerVal = getVal(dealerCards);
        System.out.println("Dealer Value: " + dealerVal);
    }

    public int evaluateStartHand(){

        // Dealer zieht 21
        if(dealerVal == 21 && dealerVal > playerVal){
            return 0;
        }

        // Player zieht 21
        if(playerVal == 21 && playerVal > dealerVal){
            return 1;
        }

        // Player & Dealer ziehen 21
        if(playerVal == 21 && dealerVal == 21){
            return 2;
        }

        // Player zieht zwei gleiche Karten
        if(playerCards[0] == playerCards[1]){
            System.out.println("Split Cards? (y/n)");
            char ans = input.next().charAt(0);
            if(ans == 'y'){
                return 3;
            }
        }

        return -1;
    }

    public int printMsg(int num){
        switch (num){
            case 0:
                System.out.println("Du hast verloren! \n Nochmal? \n JA (1) | NEIN (0)");
                break;
            case 1:
                System.out.println("Du hast gewonnen! \n Nochmal? \n JA (1) | NEIN (0)");
                break;
            case 2:
                System.out.println("Unentschieden! \n Nochmal? \n JA (1) | NEIN (0)");
                break;
        }
        int res = input.nextInt();
        if(res == 0){
            return 0;
        } else if (res == 1) {
            return 1;
        }
        return -1;
    }

    public int play() throws InterruptedException{

        char optr = ' ';
        int startP = 2;
        int startD = 2;

        // Spielvorgang
        while(optr != 's'){

            System.out.println("Hit (h), Stand (s)");
            optr = input.next().charAt(0);

            switch (optr){
                case 'h':
                    // Wait 2 seconds
                    TimeUnit.SECONDS.sleep(2);

                    // Fill Array with new Card
                    generateCards(1, startP, 1);
                    startP++;

                    // Get new Value
                    playerVal = getVal(playerCards);
                    // Wenn über 21 -> check for Ace and flip
                    if(playerVal > 21 && checkForAce(playerCards)){
                        flipAce(playerCards);
                        playerVal = getVal(playerCards);
                    }
                    System.out.println("Du hast: " + playerVal);

                    // Scenarios:
                    // Bust
                    if(playerVal > 21){
                        return 0;
                    }
                    // New Card
                    break;
                case 's':
                    // Dealer pulls to beat player
                    while(dealerVal < 21 && dealerVal < playerVal){

                        // Wait 2 seconds
                        TimeUnit.SECONDS.sleep(2);

                        // Fill Array with new Card
                        generateCards(1, startD, 2);
                        startD++;

                        // Get new Value
                        dealerVal = getVal(dealerCards);
                        // Wenn über 21 -> check for Ace and flip
                        if(dealerVal > 21 && checkForAce(dealerCards)){
                            flipAce(dealerCards);
                            dealerVal = getVal(dealerCards);
                        }
                        System.out.println("Dealer Value: " + dealerVal);
                    }

                    // Scenarios:
                    // Dealer busts
                    if(dealerVal > 21){
                        return 1;
                    }
                    // Dealer wins
                    if(dealerVal > playerVal && dealerVal <= 21){
                        return 0;
                    }
                    // Tie
                    if(dealerVal == playerVal){
                        return 2;
                    }
                    break;
                case 'q':
                    return -1;
                default:
                    System.out.println("Bitte korrekte Eingabe machen!");
            }

        }
        return -1;
    }


    public void generateCards(int number, int start, int actor){

        // Generate number between 1 and 11 and write to Array
        for(int i = start; i < start+number; i++){
            double rndm = Math.round(13 * Math.random());
            if(rndm < 2){
                rndm = 2;
            }
            if(rndm > 11) {
                rndm = 11;
            }

            // Print Cards
            if(rndm < 11){
                System.out.println(rndm);
            }
            if(rndm == 11){
                System.out.println(rndm + " | 1.0");
            }

            // Fill Array with Cards
            if(actor == 1){
                playerCards[i] = rndm;
            }
            if(actor == 2){
                dealerCards[i] = rndm;
            }

        }
        // Adjust Value in first Round
        if(start == 0 && actor == 1){
            checkForAceStart(playerCards);
        }
        if(start == 0 && actor == 2){
            checkForAceStart(dealerCards);
        }
    }

    public void checkForAceStart(double[] cards){
        // Both Cards Ace
        if(cards[0] == 11 && cards[1] == 11){
            cards[0] = 1;
        }
    }

    public boolean checkForAce(double[] cards){
        for (double card : cards) {
            if (card == 11) {
                return true;
            }
        }
        return false;
    }

    public void flipAce(double[] cards){
        for(int i = 0; i < cards.length; i++){
            if(cards[i] == 11){
                cards[i] = 1;
            }
        }
    }

    public double getVal(double[] cards){
        // Get Value of all cards in cards[]
        double val = 0;
        for (double card : cards) {
            val = val + card;
        }
        return val;
    }

    public void cleanCards(){
        for(int i = 0; i < playerCards.length ; i++){
            playerCards[i] = 0;
            dealerCards[i] = 0;
        }
    }
}
