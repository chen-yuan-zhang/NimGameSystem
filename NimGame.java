/*
   The University of Melbourne
   School of Computing and Information Systems
   COMP90041 Programming and Software Development
   Lecturer: Prof. Rui Zhang
   Semester 1, 2018
   Project C
   Author: Chenyuan Zhang
   Student ID: 815901
*/

import java.util.Scanner;
import java.util.InputMismatchException;

/*
Class of Game Operations
Two players and the terminal are needed to create a new game
The number of stone and upperBound should also be provided
 */
public class NimGame implements Game {
    private int stoneNum, upperBound;
    private NimPlayer player1, player2;
    private Scanner terminal;

    //Constructor
    public NimGame(int stoneNum, int upperBound, NimPlayer player1,
                   NimPlayer player2, Scanner terminal) {
        this.stoneNum = stoneNum;
        this.upperBound = upperBound;
        this.player1 = player1;
        this.player2 = player2;
        this.terminal = terminal;
    }

    //Get the terminal
    public Scanner getTerminal() {
        return terminal;
    }

    //Get the number of stones
    public int getStoneNum() {
        return stoneNum;
    }

    //Get the upper bound number of remove action
    public int getUpperBound() {
        return upperBound;
    }

    //Display game information
    private void printGameInfo() {
        System.out.println();

        System.out.println("Initial stone count: " + stoneNum);
        System.out.println("Maximum stone removal: " + upperBound);
        System.out.println("Player 1: " + player1.getName());
        System.out.println("Player 2: " + player2.getName());

        System.out.println();
    }

    //Display stones
    private void printStones() {
        System.out.print(stoneNum + " stones left:");

        for (int i = 0; i < stoneNum; i++)
            System.out.print(" *");

        System.out.println();
    }

    //Remove stones
    private void remove(NimPlayer p) {
        boolean done = false;
        int removeNum;
        //Max number of stones can be removed in this action
        int maxNum = (upperBound < stoneNum) ? upperBound : stoneNum;

        while (!done) {
            try {
                System.out.println(p.getGivenname() + "'s turn - remove how many?");

                //Player does remove action on this game
                //InvlidInputException may be thrown if human player input a non-integer
                removeNum = p.removeStone(this);

                if ((maxNum < removeNum) || (removeNum <= 0))     //Remove failed
                    throw new InvalidInputException();

                //Remove successful
                stoneNum = stoneNum - removeNum;
                done = true;
            } catch (InvalidInputException e) {
                //Catch all possible invalid input(include non-integer and out of bound)
                System.out.println();
                System.out.println(e.getMessage() + " You must remove between 1 and "
                        + maxNum + " stones.");
                System.out.println();

                printStones();
            }
        }

    }

    //Check if stones is empty
    private boolean isEmpty() {
        return (stoneNum == 0);
    }

    //Start a game
    public void startGame() {
        printGameInfo();
        player1.playGame();
        player2.playGame();

        NimPlayer currentPlayer = player1;          //Begin with player1

        //Game ends when no stone left
        while (!isEmpty()) {
            printStones();

            remove(currentPlayer);          //Current player does remove action
            System.out.println();

            currentPlayer = (currentPlayer == player1) ? player2 : player1;     //Change player
        }

        System.out.println("Game Over");
        System.out.println(currentPlayer.getName() + " wins!");

        currentPlayer.winGame();            //Update the statistics
    }

}
