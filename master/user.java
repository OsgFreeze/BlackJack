package master;

import java.util.Scanner;

public class user {

    public int balance = 0;
    Scanner scanner = new Scanner(System.in);

    public void setBalance(int amt){
        this.balance = amt;
    }

    public int getBalance(){
        return this.balance;
    }

    public void bet(){
        System.out.println("Place your bet: ");
        int bet = scanner.nextInt();
        if(bet > balance){
            System.out.println("You only have " + balance + " Chips");
        }else{
            setBalance(balance - bet);
            System.out.println("You placed " + bet + " Chips \nTotal Chips: " + balance);
        }
    }

    public void win(){

    }

    public void loose(){

    }

}
