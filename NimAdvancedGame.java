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


import java.util.StringTokenizer;
import java.util.Scanner;
import java.util.InputMismatchException;

/*
Class of Advanced Game Operations
Two players and the terminal are needed to create a new game
The number of stones should also be provided
 */

public class NimAdvancedGame implements Game {
    private int stoneNum;
    private boolean[] stones;
    private NimPlayer player1, player2;
    private Scanner terminal;
    private static final int MAX_NUM = 2;

    //Constructor
    public NimAdvancedGame(int stoneNum, NimPlayer player1, NimPlayer player2,
                           Scanner terminal) {
        this.stoneNum = stoneNum;
        stones = new boolean[stoneNum];
        for (int i = 0; i < stoneNum; i++)
            stones[i] = true;

        this.player1 = player1;
        this.player2 = player2;
        this.terminal = terminal;
    }

    //Get the terminal
    public Scanner getTerminal() {
        return terminal;
    }

    //Get the state of stones
    public boolean[] getStones() {
        return stones;
    }

    //Display game information
    private void printGameInfo() {
        System.out.println();

        System.out.println("Initial stone count: " + stoneNum);

        System.out.print("Stones display:");
        for (int i = 1; i <= stones.length; i++) {
            System.out.print(" <" + i + ",*>");
        }
        System.out.println();

        System.out.println("Player 1: " + player1.getName());
        System.out.println("Player 2: " + player2.getName());

        System.out.println();
    }

    //Display stones
    private void printStones() {
        System.out.print(stoneNum + " stones left:");

        for (int i = 1; i <= stones.length; i++) {
            if (stones[i - 1])
                System.out.print(" <" + i + ",*>");
            else
                System.out.print(" <" + i + ",x>");
        }

        System.out.println();
    }

    //Check if stones is empty
    private boolean isEmpty() {
        return (stoneNum == 0);
    }

    //Remove stones
    private void remove(NimPlayer p) {
        boolean done = false;
        StringTokenizer removeInfo;
        int position, num;

        while (!done) {
            try {
                System.out.println(p.getGivenname() + "'s turn - which to remove?");

                removeInfo = p.removeStone(this);

                position = Integer.parseInt(removeInfo.nextToken());
                num = Integer.parseInt(removeInfo.nextToken());

                if ((num > MAX_NUM) || (num < 1) || (position < 1) ||
                        ((position + num - 1) > stones.length))
                    throw new InvalidInputException();

                for (int i = 0; i < num; i++) {
                    if (!stones[position + i - 1])
                        throw new InvalidInputException();
                }

                for (int i = 0; i < num; i++) {
                    stones[position + i - 1] = false;
                }
                stoneNum -= num;
                done = true;


            } catch (InvalidInputException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();

                printStones();
            }
        }
    }

    //Start a game
    public void startGame() {
        printGameInfo();
        player1.playGame();
        player2.playGame();

        NimPlayer currentPlayer = player2;  //Actually begin with player1 since change player first

        while (!isEmpty())           //Game ends when no stone left
        {
            currentPlayer = (currentPlayer == player1) ? player2 : player1;     //Change player

            printStones();

            remove(currentPlayer);          //Current player does remove action
            System.out.println();
        }

        System.out.println("Game Over");
        System.out.println(currentPlayer.getName() + " wins!");

        currentPlayer.winGame();            //Update the statistics
    }
}
