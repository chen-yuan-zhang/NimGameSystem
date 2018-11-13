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

public class InvalidCommandException extends Exception {
    public InvalidCommandException() {
        super("Invalid command.");
    }

    public InvalidCommandException(String message) {
        super("'" + message + "' is not a valid command.");
    }
}
