package master;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class agent {

    static user player = new user();
    static stack stackOne = new stack();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Guten Tag!");
        player.balance = 10000;
        int state;

        while(true){

            player.bet();

            stackOne.dealStartHand();
            int sRes = stackOne.evaluateStartHand();
            state = handleHand(sRes);
            if(state < 0){
                return;
            } else if (state > 0) {
                continue;
            }

            // Check if you won already

            int pRes = stackOne.play();
            state = handleHand(pRes);
            if(state < 0){
                return;
            }

            stackOne.cleanCards();
        }
    }

    public static int handleHand(int res){
        switch (res){
            case 0:
                // You loose

                // Adjust Balance
                player.loose();

                // End-Message
                int x = stackOne.printMsg(0);
                if(x <= 0){
                    // Quit
                    return -1;
                }
                // Continue
                return 1;
            case 1:
                // You win

                // Adjust-Balance
                player.win();

                // End-Message
                int y = stackOne.printMsg(1);
                if(y <= 0){
                    // Quit
                    return -1;
                }
                // Continue
                return 1;
            case 2:
                // Tie

                // Adjust Balance
                player.tie();

                // End-Message
                int z = stackOne.printMsg(2);
                if(z <= 0){
                    // Quit
                    return -1;
                }
                // Continue
                return 1;
            case 3:
                // Split Hand

                // Adjust Balance

                // Second Stack
                stack stackTwo = new stack();
            default:
                // Continue to play
                return 0;
        }
    }

}
