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

public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid move.");
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
