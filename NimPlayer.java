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

/*
Class to store information of each player
Username, Family name and Given name are needed to create an account
 */

import java.io.Serializable;
import java.util.StringTokenizer;

public abstract class NimPlayer implements Comparable<NimPlayer>, Serializable {
    private String username, familyname, givenname;
    private int numOfGame, numOfWon;

    //No-arg Constructor
    public NimPlayer() {
    }

    //Normal Constructor
    public NimPlayer(String username, String familyname, String givenname) {
        this.username = username;
        this.familyname = familyname;
        this.givenname = givenname;
        numOfGame = 0;
        numOfWon = 0;
    }

    //Get username
    public String getUsername() {
        return username;
    }

    //Get full name
    public String getName() {
        return givenname + " " + familyname;
    }

    //Get givenname
    public String getGivenname() {
        return givenname;
    }

    //Get number of games
    public int getNumOfGame() {
        return numOfGame;
    }

    //Get number of won
    public int getNumOfWon() {
        return numOfWon;
    }

    //Get win ratio for display
    public String getWinRatio() {
        return Math.round(numOfWon / (double) numOfGame * 100) + "%";
    }


    //Edit familyname and givenname
    public void setName(String newfamilyname, String newgivenname) {
        familyname = newfamilyname;
        givenname = newgivenname;
    }

    //Reset the statistics
    public void resetStatistics() {
        numOfGame = 0;
        numOfWon = 0;
    }

    //Information Display
    public String toString() {
        return username + "," + givenname + "," + familyname + "," + numOfGame
                + " games," + numOfWon + " wins";
    }

    //Do remove action on the game
    public abstract int removeStone(NimGame game) throws InvalidInputException;

    //Do remove action on the advanced game
    public abstract StringTokenizer removeStone(NimAdvancedGame game);


    //Update the statistics after winning a game
    public void winGame() {
        numOfWon++;
    }

    //Update the statistics when playing a game
    public void playGame() {
        numOfGame++;
    }

    //Lexicographic Order of username
    public int compareTo(NimPlayer player) {
        return this.username.compareTo(player.username);
    }

}
