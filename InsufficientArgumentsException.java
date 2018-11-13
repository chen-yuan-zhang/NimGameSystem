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

public class InsufficientArgumentsException extends Exception {
    public InsufficientArgumentsException() {
        super("Incorrect number of arguments supplied to command.");
    }

    public InsufficientArgumentsException(String message) {
        super(message);
    }
}
