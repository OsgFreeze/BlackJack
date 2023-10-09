package master;

import java.util.Scanner;

public class user {

    public int balance = 0;
    public int bet = 0;

    public double[] pCards1 = new double[10];
    public double pVal1;

    public double[] pCards2 = new double[10];
    public double pVal2;

    Scanner scanner = new Scanner(System.in);

    public void setBalance(int amt){
        this.balance = amt;
    }

    public void printBalance(){
        System.out.println("You have " + balance + " Chips");
    }

    public boolean bet(){
        System.out.println("Place your bet: ");
        bet = scanner.nextInt();
        if(bet > balance){
            System.out.println("You only have " + balance + " Chips");
            return false;
        }else{
            setBalance(balance - bet);
            System.out.println("You placed " + bet + " Chips \nTotal Chips: " + balance);
            return true;
        }
    }

    public boolean split(){
        if(balance >= (bet)){
            System.out.println("Cards split \nBet another " + bet);
            setBalance(balance - bet);
            return true;
        }else {
            System.out.println("Not enough chips to split");
            return false;
        }
    }

    public void win(){
        setBalance(balance + (2*bet));
        System.out.println("You won " + (2*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void doubleWin(){
        setBalance(balance + (4*bet));
        System.out.println("You won " + (4*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void tripleWin(){
        setBalance(balance + (6*bet));
        System.out.println("You won " + (6*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void looseOne(){
        setBalance(balance + (2*bet));
        System.out.println("You lost " + bet + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void looseTwo(){
        setBalance(balance + bet);
        System.out.println("You lost " + (2*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void looseAll(){
        System.out.println("You lost " + bet + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }

    public void tie(int num){
        setBalance(balance + (num*bet));
        System.out.println("Its a Tie! You regain " + (num*bet) + " Chips \nTotal Chips: " + balance);
        bet = 0;
    }
}
