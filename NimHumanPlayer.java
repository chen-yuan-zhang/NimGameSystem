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
import java.util.StringTokenizer;

/*
Class to store information of human player.
*/

public class NimHumanPlayer extends NimPlayer {
    //No-arg Constructor
    public NimHumanPlayer() {
    }

    //Normal Constructor
    public NimHumanPlayer(String username, String familyname, String givenname) {
        super(username, familyname, givenname);
    }

    //Do remove action on the game
    public int removeStone(NimGame game) throws InvalidInputException {
        Scanner terminal = game.getTerminal();
        try {
            if (!terminal.hasNextInt()) throw new InvalidInputException();
            return terminal.nextInt();      //Only read the first characters, ignore the rest
        } finally {
            //Whether the input is an integer or not, get rid of the end of the line
            terminal.nextLine();
        }
    }

    //Do remove action on the advanced game
    public StringTokenizer removeStone(NimAdvancedGame game) {
        Scanner terminal = game.getTerminal();
        String input = terminal.nextLine();

        //Assume the input is valid(two integers split with whitespace)
        return (new StringTokenizer(input));
    }
}
