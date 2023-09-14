package master;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class test {

    static Scanner input = new Scanner(System.in);
    static double[] playerCards = new double[10];
    static double[] dealerCards = new double[10];

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Guten Tag!");

        while(true){

            cleanCards();
            double playerVal;
            double dealerVal;
            char optr = ' ';
            int startP = 2;
            int startD = 2;

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

            // Dealer zieht 21
            if(dealerVal == 21 && dealerVal > playerVal){
                printMsg(0);
                int x = input.nextInt();
                if(x == 0){
                    return;
                }
                continue;
            }

            // Player zieht 21
            if(playerVal == 21 && playerVal > dealerVal){
                printMsg(1);
                int x = input.nextInt();
                if(x == 0){
                    return;
                }
                continue;
            }

            // Player & Dealer ziehen 21
            if(playerVal == 21 && dealerVal == 21){
                printMsg(2);
                int x = input.nextInt();
                if(x == 0){
                    return;
                }
                continue;
            }

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
                            printMsg(0);
                            int x = input.nextInt();
                            if(x == 0) {
                                return;
                            }
                            optr = 's';     // breaks while -> Game restart
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
                            printMsg(1);
                            int x = input.nextInt();
                            if(x == 0){
                                return;
                            }
                        }
                        // Dealer wins
                        if(dealerVal > playerVal && dealerVal <= 21){
                            printMsg(0);
                            int x = input.nextInt();
                            if(x == 0){
                                return;
                            }
                        }
                        // Tie
                        if(dealerVal == playerVal){
                            printMsg(2);
                            int x = input.nextInt();
                            if(x == 0){
                                return;
                            }
                        }
                        break;
                    case 'q':
                        return;
                    default:
                        System.out.println("Bitte korrekte Eingabe machen!");
                }

            }
        }
    }

    public static void generateCards(int number, int start, int actor){

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
            // TODO: Player zieht zwei gleiche Karten -> splitCards statt checkForAceStart aufrufen
        }
        if(start == 0 && actor == 2){
            checkForAceStart(dealerCards);
        }
    }

    public static void checkForAceStart(double[] cards){
        // Both Cards Ace
        if(cards[0] == 11 && cards[1] == 11){
            cards[0] = 1;
        }
    }

    public static void splitCards(double[] cards){
        if(cards[0] == cards[1]){
            System.out.println("Do you want to Split? (y/n)");
            char optr = input.next().charAt(0);
            switch (optr){
                case 'y':
                    double[] stackOne = new double[10];
                    double[] stackTwo = new double[10];
                    stackOne[0] = cards[0];
                    stackTwo[0] = cards[0];
                    // TODO: Spielvorgang in eigene Klasse auslagern und dann hier zwei mal aufrufen
                    break;
                case 'n':
                    break;
            }
        }
    }

    public static boolean checkForAce(double[] cards){
        for (double card : cards) {
            if (card == 11) {
                return true;
            }
        }
        return false;
    }

    public static void flipAce(double[] cards){
        for(int i = 0; i < cards.length; i++){
            if(cards[i] == 11){
                cards[i] = 1;
            }
        }
    }

    public static double getVal(double[] cards){
        // Get Value of all cards in cards[]
        double val = 0;
        for (double card : cards) {
            val = val + card;
        }
        return val;
    }

    public static void printMsg(int num){
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
    }

    public static void cleanCards(){
        for(int i = 0; i < playerCards.length ; i++){
            playerCards[i] = 0;
            dealerCards[i] = 0;
        }
    }

}
