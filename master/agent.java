package master;

import java.io.IOException;

public class agent {

    static user player = new user();
    static stack stackOne = new stack();
    static api aapi = new api();

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Guten Tag!");
        player.balance = aapi.apiget();

        int state;

        while(true){

            player.printBalance();
            boolean valid = player.bet();
            if(!valid){
                continue;
            }

            // deal and evaluate start Hand
            stackOne.dealStartHand();
            int sRes = stackOne.evaluateStartHand();
            state = handleHand(sRes);
            if(state < 0){
                return;
            } else if (state > 0) {
                continue;
            }

            // play Game and evaluate Outcome
            int pRes = stackOne.play();
            state = handleHand(pRes);
            if(state < 0){
                return;
            }

            // clean Arrays
            stackOne.cleanCards();
        }
    }

    public static int handleHand(int res) throws IOException {
        switch (res){
            case 0:
                // You loose

                // Adjust Balance
                player.loose();
                if(player.balance == 0){
                    return -1;
                }

                // End-Message
                int x = stackOne.printMsg();
                if(x <= 0){
                    // Quit
                    updateBalance();
                    return -1;
                }
                // Continue
                return 1;
            case 1:
                // You win

                // Adjust-Balance
                player.win();

                // End-Message
                int y = stackOne.printMsg();
                if(y <= 0){
                    // Quit
                    updateBalance();
                    return -1;
                }
                // Continue
                return 1;
            case 2:
                // Tie

                // Adjust Balance
                player.tie();

                // End-Message
                int z = stackOne.printMsg();
                if(z <= 0){
                    // Quit
                    updateBalance();
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

    public static void updateBalance() throws IOException {
        aapi.apidelete();
        aapi.apipost(player.balance);
    }

}
