package master;

import java.util.Scanner;

public class user {

    public int balance = 0;
    public int bet = 0;
    Scanner scanner = new Scanner(System.in);

    public void setBalance(int amt){
        this.balance = amt;
    }

    public void bet(){
        System.out.println("Place your bet: ");
        bet = scanner.nextInt();
        if(bet > balance){
            System.out.println("You only have " + balance + " Chips");
        }else{
            setBalance(balance - bet);
            System.out.println("You placed " + bet + " Chips \nTotal Chips: " + balance);
        }
    }

    public void win(){
        setBalance(balance + (2*bet));
        System.out.println("You won " + (2*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void loose(){
        System.out.println("You lost " + bet + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void tie(){
        setBalance(balance + bet);
        System.out.println("Its a Tie! You regain " + bet + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

}
