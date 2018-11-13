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
     The system of the game of Nim(2-player version, including advanced version)
     Addplayer, addaiplayer, removeplayer, editplayer, resetstats, displayplayer,
     rankings, startgame, startadvancedgame, exit
     are commands supported by the system
     The max number of players is 100 and the max number of games per player is 99.
*/

import java.util.*;
import java.io.*;

public class Nimsys {
    enum Command {
        ADDPLAYER, ADDAIPLAYER, REMOVEPLAYER, EDITPLAYER, RESETSTATS,
        DISPLAYPLAYER, RANKINGS, STARTGAME, STARTADVANCEDGAME, EXIT
    }           //Valid Commands


    //Numbers of Arguments for commands
    private static final byte ADD_ARG_NUM = 3;
    private static final byte REMOVE_ARG_NUM = 1;
    private static final byte EDIT_ARG_NUM = 3;
    private static final byte RESET_ARG_NUM = 1;
    private static final byte DISPLAY_ARG_NUM = 1;
    private static final byte START_ARG_NUM = 4;
    private static final byte START_ADV_ARG_NUM = 3;


    private static final String FILE_NAME = "players.dat";
    private static final int MAX_PLAYER_NUM = 100;
    private static final int MAX_LIST_NUM = 10;
    private static final int MAX_ADV_STONES = 11;
    private static final char COMMAND_PROMPT = '$';
    private static final char YES_FLAG = 'y';

    private Scanner terminal;
    private NimPlayer[] playerList;
    private int numOfPlayer;
    private Game currentGame;

    //Constructor
    public Nimsys() {
        terminal = new Scanner(System.in);
        readFile();
        SG.init(MAX_ADV_STONES);          //Prepare the AI system for advanced game
    }


    //Add a human player to playerlist if not exists the same username
    public void addPlayer(String username, String familyname, String givenname) {
        if (findUser(username) != null)
            System.out.println("The player already exists.");
        else {
            playerList[numOfPlayer] = new NimHumanPlayer(username, familyname, givenname);
            numOfPlayer++;
        }
    }

    //Add a AI player to playerlist if not exists the same username
    public void addAIPlayer(String username, String familyname, String givenname) {
        if (findUser(username) != null)
            System.out.println("The player already exists.");
        else {
            playerList[numOfPlayer] = new NimAIPlayer(username, familyname, givenname);
            numOfPlayer++;
        }
    }

    //Remove a player from playerlist if exists
    public void removePlayer(String username) {
        int position = findUserIndex(username);    //position in array is needed to remove

        if (position >= 0) {
            //Replace the player with the last player
            playerList[position] = playerList[numOfPlayer - 1];
            numOfPlayer--;
        } else
            System.out.println("The player does not exist.");
    }

    //Remove all Players
    public void removePlayer() {
        System.out.println("Are you sure you want to remove all players? (y/n)");
        if (terminal.next().charAt(0) == YES_FLAG) {
            numOfPlayer = 0;
        }
    }

    //Edit a player's name if exists
    public void editPlayer(String username, String newFamilyname, String newGivenname) {
        NimPlayer player = findUser(username);

        if (player != null)
            player.setName(newFamilyname, newGivenname);
        else
            System.out.println("The player does not exist.");
    }

    //Reset a player's statistics if exists
    public void resetStats(String username) {
        NimPlayer player = findUser(username);

        if (player != null)
            player.resetStatistics();
        else
            System.out.println("The player does not exist.");
    }

    //Reset all players' statistics
    public void resetStats() {
        System.out.println("Are you sure you want to reset all player statistics? (y/n)");

        if (terminal.next().charAt(0) == YES_FLAG)
            for (int i = 0; i < numOfPlayer; i++)
                playerList[i].resetStatistics();
    }

    //Display the information of a player if exists
    public void displayPlayer(String username) {
        NimPlayer player = findUser(username);

        if (player != null)
            System.out.println(player);
        else
            System.out.println("The player does not exist.");
    }

    //Display all players' information
    public void displayPlayer() {
        //Sort the player by lexicographic order
        Arrays.sort(playerList, 0, numOfPlayer);

        for (int i = 0; i < numOfPlayer; i++)
            System.out.println(playerList[i]);
    }

    /*
    Display a list of player rankings
    asc is true means ascending order
    asc is false means descending order

    Whether ascending or descending winning ratio,
    tie will be solved by ascending order of username.
    Two comparators are used to satisfy this requirement.
    */


    public void playerRanking(boolean asc) {
        if (asc)        //Ascending order
            Arrays.sort(playerList, 0, numOfPlayer, new WinRatioAscOrder());
        else            //Descending order
            Arrays.sort(playerList, 0, numOfPlayer, new WinRatioDescOrder());

        int numOfList = (numOfPlayer < MAX_LIST_NUM) ? numOfPlayer : MAX_LIST_NUM;

        for (int i = 0; i < numOfList; i++)
            System.out.printf("%-5s| %02d games | %s%n", playerList[i].getWinRatio(),
                    playerList[i].getNumOfGame(), playerList[i].getName());
    }

    //Start a new game if two players exist
    public void startNewGame(int numOfStone, int upperBound, String username1, String username2) {
        NimPlayer player1 = findUser(username1);
        NimPlayer player2 = findUser(username2);

        if ((player1 != null) && (player2 != null)) {
            currentGame = new NimGame(numOfStone, upperBound,
                    player1, player2, terminal);
            currentGame.startGame();
        } else
            System.out.println("One of the players does not exist.");
    }

    //Start a new advanced game if two players exist
    public void startNewAdvancedGame(int numOfStone, String username1, String username2) {
        NimPlayer player1 = findUser(username1);
        NimPlayer player2 = findUser(username2);

        if ((player1 != null) && (player2 != null)) {
            currentGame = new NimAdvancedGame(numOfStone,
                    player1, player2, terminal);
            currentGame.startGame();
        } else
            System.out.println("One of the players does not exist.");
    }

    //Read the information of players from the file
    public void readFile() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(FILE_NAME));
            playerList = (NimPlayer[]) inputStream.readObject();
            numOfPlayer = inputStream.readInt();
            inputStream.close();
        } catch (FileNotFoundException e) {
            //No existing file
            playerList = new NimPlayer[MAX_PLAYER_NUM];
            numOfPlayer = 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    //Save the information of players into the file
    public void saveFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME));
            outputStream.writeObject(playerList);
            outputStream.writeInt(numOfPlayer);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    Search username in playerlist
    Return the player if exists, return null if not exists
    */
    private NimPlayer findUser(String username) {
        for (int i = 0; i < numOfPlayer; i++)
            if (username.equals(playerList[i].getUsername()))
                return playerList[i];

        return null;
    }

    /*
    Search username in playerlist
    Return the position if exists, return -1 if not exists
    This method is for remove command
    */
    private int findUserIndex(String username) {
        for (int i = 0; i < numOfPlayer; i++)
            if (username.equals(playerList[i].getUsername()))
                return i;

        return -1;
    }

    //Check if the command is valid
    private boolean isValidCommand(String command) {
        Command[] commandList = Command.values();
        for (Command c : commandList) {
            if (c.toString().equals(command.toUpperCase())) return true;
        }
        return false;
    }

    //Comparator for winning ratio ascending order
    public class WinRatioAscOrder implements Comparator<NimPlayer> {
        public int compare(NimPlayer p1, NimPlayer p2) {
            //Use multiplication to avoid fraction for exact comparision
            int compareWinRatio = p1.getNumOfWon() * p2.getNumOfGame()
                    - p2.getNumOfWon() * p1.getNumOfGame();

            if (compareWinRatio == 0) {
                //Two possibilities need to be considered here


                //If one player have not played any games yet,
                //and the other player won at least one game

                if ((p1.getNumOfGame() * p2.getNumOfGame() == 0) &&
                        (p1.getNumOfWon() + p2.getNumOfWon() > 0))
                    return p1.getNumOfWon() - p2.getNumOfWon();

                //Otherwise, tie would be solved by lexicographic order
                return p1.compareTo(p2);
            }

            return compareWinRatio;
        }
    }


    //Comparator for winning ratio descending order
    public class WinRatioDescOrder implements Comparator<NimPlayer> {
        public int compare(NimPlayer p1, NimPlayer p2) {
            //Use multiplication to avoid fraction for exact comparision
            int compareWinRatio = p2.getNumOfWon() * p1.getNumOfGame()
                    - p1.getNumOfWon() * p2.getNumOfGame();

            if (compareWinRatio == 0) {
                //Two possibilities need to be considered here


                //If one player have not played any games yet,
                //and the other player won at least one game

                if ((p1.getNumOfGame() * p2.getNumOfGame() == 0) &&
                        (p1.getNumOfWon() + p2.getNumOfWon() > 0))
                    return p2.getNumOfWon() - p1.getNumOfWon();

                //Otherwise, tie would be solved by lexicographic order
                return p1.compareTo(p2);
            }

            return compareWinRatio;
        }
    }


    //Run System
    public static void main(String[] args) {
        Nimsys nimSys = new Nimsys();
        System.out.println("Welcome to Nim");

        Command currentCommand;
        String commandString, arguments;
        StringTokenizer argList;


        //Loop until exit
        //Commands are not case-sensitive. Arguments are case-sensitive
        //If arguments are more than wanted, just ignore the surplus
        while (true) {
            System.out.println();
            System.out.print(COMMAND_PROMPT);
            commandString = nimSys.terminal.next();

            try {
                if (!nimSys.isValidCommand(commandString))
                    throw new InvalidCommandException(commandString);
                currentCommand = Command.valueOf(commandString.toUpperCase());
                arguments = nimSys.terminal.nextLine().trim();             //Remove the white space
                argList = new StringTokenizer(arguments, ",");

                switch (currentCommand) {
                    case ADDPLAYER:
                        if (argList.countTokens() >= ADD_ARG_NUM)
                            nimSys.addPlayer(argList.nextToken(), argList.nextToken(),
                                    argList.nextToken());
                        else
                            throw new InsufficientArgumentsException();

                        break;

                    case ADDAIPLAYER:
                        if (argList.countTokens() >= ADD_ARG_NUM)
                            nimSys.addAIPlayer(argList.nextToken(), argList.nextToken(),
                                    argList.nextToken());
                        else
                            throw new InsufficientArgumentsException();

                        break;

                    case REMOVEPLAYER:
                        if (argList.countTokens() >= REMOVE_ARG_NUM)
                            nimSys.removePlayer(argList.nextToken());
                        else
                            nimSys.removePlayer();

                        break;

                    case EDITPLAYER:
                        if (argList.countTokens() >= EDIT_ARG_NUM)
                            nimSys.editPlayer(argList.nextToken(), argList.nextToken(),
                                    argList.nextToken());
                        else
                            throw new InsufficientArgumentsException();

                        break;

                    case RESETSTATS:
                        if (argList.countTokens() >= RESET_ARG_NUM)
                            nimSys.resetStats(argList.nextToken());
                        else
                            nimSys.resetStats();

                        break;

                    case DISPLAYPLAYER:
                        if (argList.countTokens() >= DISPLAY_ARG_NUM)
                            nimSys.displayPlayer(argList.nextToken());
                        else
                            nimSys.displayPlayer();

                        break;


                    //Only when "asc" is the first arguments, ascending order ranking will be done.
                    //Otherwise descending ranking will be done.

                    case RANKINGS:
                        if (argList.hasMoreTokens())
                            nimSys.playerRanking(argList.nextToken().equals("asc"));
                        else            //No arguments
                            nimSys.playerRanking(false);

                        break;

                    case STARTGAME:
                        if (argList.countTokens() >= START_ARG_NUM)
                            //Convert first two arguments to integer
                            nimSys.startNewGame(Integer.parseInt(argList.nextToken()),
                                    Integer.parseInt(argList.nextToken()),
                                    argList.nextToken(), argList.nextToken());
                        else
                            throw new InsufficientArgumentsException();

                        break;

                    case STARTADVANCEDGAME:
                        if (argList.countTokens() >= START_ADV_ARG_NUM)
                            //Convert first argument to integer
                            nimSys.startNewAdvancedGame(Integer.parseInt(argList.nextToken()),
                                    argList.nextToken(), argList.nextToken());
                        else
                            throw new InsufficientArgumentsException();

                        break;


                    case EXIT:
                        System.out.println();
                        nimSys.saveFile();
                        System.exit(0);
                }

            } catch (InvalidCommandException e) {
                //The exception would be thrown if input an invalid command
                System.out.println(e.getMessage());
                nimSys.terminal.nextLine();
            } catch (InsufficientArgumentsException e) {
                //If arguments for the command is insufficient
                System.out.println(e.getMessage());
            }

        }

    }

}
