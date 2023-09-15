package master;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class agent {

    static Scanner input = new Scanner(System.in);
    static double[] playerCards = new double[10];
    static double[] dealerCards = new double[10];
    static user player = new user();
    static stack stackOne = new stack();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Guten Tag!");
        player.balance = 10000;
        player.bet();

        stackOne.play();

    }

}
