package master;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class agent {

    static user player = new user();
    static dealer dealer = new dealer();
    static stack stack = new stack();
    static api aapi = new api();

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Guten Tag!");
        player.balance = aapi.apiget();
        if(player.balance == 0){
            aapi.apidelete();
            aapi.apipost(10000);
            player.balance = aapi.apiget();
            // ToDo: implement a system to repay your loan at a certain balance eg. 30000k
        }
        TimeUnit.SECONDS.sleep(3);

        while (true){
            boolean split = false;

            player.printBalance();
            if(!player.bet()){
                continue;
            }

            stack.dealStartHand(player.pCards1);
            player.pVal1 = stack.getVal(player.pCards1);

            stack.dealStartHand(dealer.dealerCards);
            dealer.dealerVal = stack.getVal(dealer.dealerCards);

            int sRes = stack.evaluateStartHand(player.pVal1, dealer.dealerVal, player.pCards1);
            if(sRes == 3){
                if(player.split()){
                    split = true;
                }
            }
            if(sRes >= 0 && sRes <= 2){
                adjustBalanceOne(sRes);
                int x = stack.printMsg();
                if(x <= 0){
                    updateBalance();
                    return;
                }
                continue;
            }

            // ToDo: Implement option to split more than once
            if(split){
                // Hand 1
                stack.dealStartHandWithCard(player.pCards1);
                player.pVal1 = stack.playerPull(player.pCards1, player.pVal1);  // returns Player Val or 0 if Player busts

                // Hand 2
                player.pCards2[0] = player.pCards1[0];
                player.pVal2 = stack.getVal(player.pCards2);

                stack.dealStartHandWithCard(player.pCards2);
                player.pVal2 = stack.playerPull(player.pCards2, player.pVal2);
                if(player.pVal1 == 0 && player.pVal2 == 0){
                    adjustBalanceTwo(2);
                    int x = stack.printMsg();
                    if(x <= 0){
                        updateBalance();
                        return;
                    }
                    continue;
                }
            }else {
                player.pVal1 = stack.playerPull(player.pCards1, player.pVal1);
                if(player.pVal1 == 0){
                    adjustBalanceOne(0);
                    int x = stack.printMsg();
                    if(x <= 0){
                        updateBalance();
                        return;
                    }
                    continue;
                }
            }

            // Dealer pulls Cards and stores final Value
            print();
            dealer.dealerVal = stack.dealerPull(dealer.dealerCards, dealer.dealerVal);

            int eRes;

            if(split){
                eRes = stack.evaluateFinalSplit(player.pVal1, player.pVal2, dealer.dealerVal); // W/W(1); W/L(2); L/W(3); L/L(4)
                adjustBalanceTwo(eRes); // Adjust balance accordingly
            } else {
                eRes = stack.evaluateFinal(player.pVal1, dealer.dealerVal);
                adjustBalanceOne(eRes);
            }

            // Clean cards
            stack.cleanCards(player.pCards2);
            stack.cleanCards(player.pCards1);
            stack.cleanCards(dealer.dealerCards);

            // Replay?
            int x = stack.printMsg();
            if(x <= 0){
                updateBalance();
                return;
            }
        }

    }

    public static void print(){
        if(dealer.dealerVal <= 16){
            System.out.println("Der Dealer hat " + dealer.dealerVal + " und zieht:");
        }else {
            System.out.println("Der Dealer hat " + dealer.dealerVal);
        }

    }

    public static void adjustBalanceOne(int res){
        switch (res) {
            case 0:
                // Loose
                player.looseAll();
                break;
            case 1:
                // Win
                player.win();
                break;
            case 2:
                // Tie
                player.tie(1);
                break;
        }
    }

    public static void adjustBalanceTwo(int res){
        switch (res) {
            case 1:
                // W/W
                System.out.println("You won both hands!");
                player.doubleWin();
                break;
            case 2:
                // L/L
                System.out.println("You lost both hands!");
                player.looseAll();
                break;
            case 3:
                // W/L - L/W
                System.out.println("You only won one hand!");
                player.tie(2);
                break;
            case 4:
                // T/T
                System.out.println("You tied both hands!");
                player.tie(2);
                break;
            case 5:
                // T/W
                System.out.println("You tied one hand and won the other!");
                player.win();
                break;
            case 6:
                // T/L
                System.out.println("You tied one hand and lost the other!");
                player.looseOne();
                break;
        }
    }

    public static void adjustBalanceThree(int res){
        switch (res) {
            case 1:
                break;
            case 2:
                break;
        }
    }


    public static void updateBalance() throws IOException {
        aapi.apidelete();
        aapi.apipost(player.balance);
    }

}
    /*


        while(true){

            player.printBalance();
            boolean valid = player.bet();
            if(!valid){
                continue;
            }

            // deal and evaluate start Hand
            stackOne.dealStartHand();
            int sRes = stackOne.evaluateStartHand();
            if(sRes == 3){
                split = true;
            }
            state = handleHand(sRes);
            if(state < 0){
                return;
            } else if (state > 0) {
                continue;
            }

            /* play Game and evaluate Outcome
            int pRes = stackOne.play(false);
            state = handleHand(pRes);
            if(state < 0){
                return;
            }

            if(split == false){
                // Player pulls cards
                int pRes = stackOne.playerPull();   // 0 End ; -1 Continue
                if(pRes == 0){
                    state = handleHand(pRes);
                    if(state < 0){
                        return;
                    }
                    stackOne.cleanCards();
                    continue;
                }
                // dealer pulls cards
                pRes = stackOne.dealerPull();   // 0,1,2 End
                state = handleHand(pRes);
                if(state < 0){
                    return;
                }
            } else {
                // Player pulls cards
                boolean stackOneActive = true;
                double Val1;

                int pRes = stackOne.playerPull();   // 0 End ; -1 Continue
                if(pRes == 0){
                    // Player overpulled
                    // ToDo: Handle loose case
                    System.out.println("Lost stack one");
                    stackOneActive = false;
                    stackOne.cleanCards();
                }
                // ToDo: seperate dealerPull and evaluate in stack.java; Dealer pulls -> check outcome against Val
                if(stackOneActive){
                    pRes = stackOne.dealerPull();
                    state = handleHand(pRes);
                    if(state < 0){
                        return;
                    }
                }else {
                    pRes = stackTwo.dealerPull();
                    state = handleHand(pRes);
                    if(state < 0){
                        return;
                    }
                }
            }

            // clean Arrays
            stackOne.cleanCards();
            stackTwo.cleanCards();

        }
    }

    public static int handleHand(int res) throws IOException, InterruptedException {
        switch (res){
            case 0:
                // You loose

                // Adjust Balance
                player.loose();
                if(player.balance == 0){
                    updateBalance();
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
                //stack stackTwo = new stack();
                int state;
                double card = stackOne.playerCards[0];
                System.out.println("Stack 2");
                stackTwo.playerCards[0] = card;

                stackTwo.dealStartHandWithCard();
                int sRes = stackTwo.evaluateStartHand();
                state = handleHand(sRes);       // Beendet ganzes Game -> Warten auf andere Hand
                if(state < 0){
                    return -1;
                } else if (state > 0) {
                    return 1;
                }

                // pull Cards and return Val
                int pRes = stackTwo.playerPull();
                if(pRes == 0){
                    // Loose second Stack cause Overpull
                    System.out.println("Lost stack 2");
                }

                // New Card for first Hand
                System.out.println("Stack 1");
                stackOne.dealStartHandWithCard();

            default:
                // Continue to play
                return 0;
        }
    }

    public static int handleHand(int res) throws IOException, InterruptedException {
        switch (res) {
            case 0:
                // You loose
                // End-Message
                int x = stack.printMsg();
                if (x <= 0) {
                    // Quit
                    return -1;
                }
                // Continue
                return 1;
            case 1:
                // You win
                System.out.println("lol");
                // End-Message
                int y = stack.printMsg();
                if (y <= 0) {
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
                int z = stack.printMsg();
                if (z <= 0) {
                    // Quit
                    return -1;
                }
                // Continue
                return 1;
            case 3:
                // Split Hand

                // Adjust Balance

                // Split


            default:
                // Continue to play
                return 0;
        }
    }



    public static void updateBalance() throws IOException {
        aapi.apidelete();
        aapi.apipost(player.balance);
    }

    public static int handleSingleHand(int res){
        switch (res){
            case 0:
                // You loose

                // Adjust Balance

                return 1;
            case 1:
                // Hold
                return 1;
            case 2:
                // Hold
                return 1;
            default:
                return 0;
        }
    }

}
*/