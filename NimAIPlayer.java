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
Class to store information of human player.
*/

import java.util.StringTokenizer;
import java.util.ArrayList;

public class NimAIPlayer extends NimPlayer implements Testable {
    //No-arg Constructor
    public NimAIPlayer() {
    }

    //Normal Constructor
    public NimAIPlayer(String username, String familyname, String givenname) {
        super(username, familyname, givenname);
    }

    //Do remove action on the game
    public int removeStone(NimGame game) {
        int numOfStone = game.getStoneNum();
        int upperBound = game.getUpperBound();

        int removeNum = (numOfStone - 1) % (upperBound + 1);

        //No victory guaranteed strategy
        if (removeNum == 0) {
            //Compute the max number of stones can be removed in this action
            //Pick a valid input randomly
            int maxNum = (upperBound < numOfStone) ? upperBound : numOfStone;
            return 1 + (int) (Math.random() * maxNum);
        }

        return removeNum;
    }

    //Do remove action on the advanced game
    public StringTokenizer removeStone(NimAdvancedGame game) {
        return new StringTokenizer(advancedMove(game.getStones(), null));
    }

    public String advancedMove(boolean[] available, String lastMove) {
        // lastMove is not necessary
        String move;

        int len = 0;
        ArrayList<Integer> stoneList = new ArrayList<>();   //Record the stone numbers of each pile
        ArrayList<Integer> position = new ArrayList<>();   //Record the start position of each pile

        //Compute the stone numbers and start position of each pile
        for (int i = 0; i < available.length; i++) {
            if (available[i]) {
                if (len == 0) position.add(i);
                len++;
            } else {
                if (len > 0) {
                    stoneList.add(len);
                    len = 0;
                }
            }
        }
        if (len > 0) stoneList.add(len);

        //Compute the remove position and number
        StringTokenizer tmp = new StringTokenizer(SG.getMove(stoneList));
        int id = Integer.parseInt(tmp.nextToken());
        int p = Integer.parseInt(tmp.nextToken());
        int num = Integer.parseInt(tmp.nextToken());
        int startPosition = position.get(id) + p + 1;

        move = startPosition + " " + num;
        return move;
    }
}

