/**
 * This program recursively solves a Sudoku puzzle, provided a solution exists.
 * The user inputs a well-formatted Sudoku puzzle from either the command-line
 * or GUI (SudokuSolverGUI) and outputs a solution, which is achieved through 
 * recursive back-tracking. If the command-line is used, then the time it took 
 * your machine to solve the problem is displayed in milliseconds. 
 * By default, the GUI is used to drive the program. If you want the command-line
 * only, then uncomment main() in SudokuSolver, then follow the instructions
 * below to compile and run.
 *
 * @author Sam Dudar, 2021
 *
 *To Compile:
 *	Execute the following command:
 *	'javac SudokuSolver.java'
 *To Run:
 *	'java SudokuSolver.java'
 */

import java.util.Scanner;
//If you don't want to use the GUI, then uncomment main() driver below,
//which then will use Scanner to fetch input from the command line.

public class SudokuSolver {
  private int[][] grid = null;
  //The game grid
  
  //Default constructor creates empty 9x9 int grid
  public SudokuSolver() {
    grid = new int[9][9];
  }
  
  //Returns completed puzzle if solved
  public int[][] getSolution() {
    if(isSolved()) {
      return grid;
    }
    else{
      return null;
    }
  }
  //Displays current state of puzzle to command line, properly formatted
  public void displayPuzzle() {
    for(int i = 0; i < 9; i++) {
      if(i % 3 == 0) {System.out.println();}
      for(int j = 0; j < 9; j++) {
        
        if(j % 3 == 0) {System.out.print(" ");}
        if(grid[i][j] == 0) {System.out.print("_ ");}
        else{System.out.print(grid[i][j] + " ");}
      }
      System.out.println();
    }
  }
  
  //Returns grid as a String, properly formatted
  @Override
  public String toString() {
    String puzzle = "";
    for(int i = 0; i < 9; i++) {
      if(i % 3 == 0) {puzzle = puzzle + "\n";}
      for(int j = 0; j < 9; j++) {
        
        if(j % 3 == 0) {puzzle = puzzle + " ";}
        if(grid[i][j] == 0) {puzzle = puzzle + "_ ";}
        else{puzzle = puzzle + grid[i][j] + " ";}
      }
      puzzle = puzzle + "\n";
    }
    return puzzle;
  }
  
  public boolean isAlreadyInRow(int value, int row) {
    boolean alreadyUsed = false;
    for(int i = 0; i < 9 && !alreadyUsed; i++) {
      alreadyUsed = (value == grid[row][i]);
    }
    return alreadyUsed;
  }

  public boolean isAlreadyInColumn(int value, int column) {
    boolean alreadyUsed = false;
    for(int i = 0; i < 9 && !alreadyUsed; i++) {
      alreadyUsed = (value == grid[i][column]);
    }
    return alreadyUsed;
  }
  
  public boolean isAlreadyInSubSquare(int value, int row, int column) {
    int[] index = indexOfSubSquare(row, column);
    boolean alreadyUsed = false;
    for(int i = 0; i < 3 && !alreadyUsed; i++) {
      for(int j = 0; j < 3 && !alreadyUsed; j++) {
        alreadyUsed = (value == grid[i + index[0]][j + index[1]]);
      }
    }
    return alreadyUsed;
  }
  
  //Returns true if value is not already in row, column, or
  //3x3 subsquare
  public boolean canPlace(int value, int row, int column) {
    boolean canPlace = isAlreadyInRow(value, row);
    if(!canPlace) {
      canPlace = isAlreadyInColumn(value, column);
      if(!canPlace) {
        canPlace = isAlreadyInSubSquare(value, row, column);
      }
    }
    return !canPlace;
  }
  
  public void setSquareValue(int value, int row, int column) {
    grid[row][column] = value;
  }
  
  public boolean isEmpty(int row, int column) {
    return grid[row][column] == 0;
  }
  
  public void clearSquare(int row, int column) {
    setSquareValue(0, row, column);
  }
  
  //Returns true if each row, column, and subsquare contains
  //a single instance of 1-9
  public boolean isSolved() {
    boolean isSolved = true;
    for(int i = 1; i <= 9 && isSolved; i++) {
      for(int j = 0; j < 9 && isSolved; j++) {
        isSolved = (isAlreadyInRow(i, j));
        if(isSolved) {
          isSolved = (isAlreadyInColumn(i, j));
        }
      }
    }
    
    if(isSolved) {
      for(int i = 1; i <= 9 && isSolved; i++) {
        for(int j = 0; j < 9 && isSolved; j += 3) {
          for(int k = 0; k < 9 && isSolved; k += 3) {
            isSolved = (isAlreadyInSubSquare(i, j, k));
          }
        }
      }
    }
    return isSolved;
  }
  

  //Returns row and column of upper leftmost number of subsquare
  public int[] indexOfSubSquare(int row, int column) {
    int subRow = 0, subColumn = 0;
    int[] index = new int[2];
    if(row < 3) {
      subRow = 0;
    }
    else if (row < 6){
      subRow = 3;
    }
    else {
      subRow = 6;
    }
    
    if(column < 3) {
      subColumn = 0;
    }
    else if (column < 6){
      subColumn = 3;
    }
    else {
      subColumn = 6;
    }
    index[0] = subRow;
    index[1] = subColumn;
    return index;
  }
  
  //Returns true if it is possible to place at least
  //one number in a given square
  public boolean canPlaceAtLeastOneNumber(int row, int column) {
    boolean canPlaceNumber = false;
        for(int k = 1; k <= 9; k++) {
          canPlaceNumber = (canPlace(k, row, column));
          if(canPlaceNumber) {break;}
        }
    return canPlaceNumber;
  }
  
  //Uses recursive backtracking to determine what values
  //should be inputted in blank spaces. Returns true if 
  //the attempted value solves the puzzle
  public boolean solvePuzzle() {
    
    //Base case: if the puzzle is solved, we're done
    if(isSolved()) {
      return true;
    }
    
    //Approach: Puzzle is not done, so if there is at least one
    //empty square in which a number can be placed,
    //place a number and return true if that solves
    //the puzzle. If that number doesn't solve the
    //puzzle, clear its square and try another number.
    //If no number can be placed or we've tried all
    //possible numbers, return false.
    else {
      boolean nowSolved = false;
      
      
      for(int i = 0; i < 9 && !nowSolved; i++) {
        for(int j = 0; j < 9 && !nowSolved; j++) {
          
          if(isEmpty(i, j)) {
            //if the square is empty
            if(!canPlaceAtLeastOneNumber(i, j)) {
              return false;
            }
            else{
              for(int k = 1; k <= 9 && !nowSolved; k++) {
                
                if(canPlace(k, i, j)) {
                  setSquareValue(k, i, j);
                  nowSolved = solvePuzzle();
                  if(!nowSolved) {
                    clearSquare(i, j);
                  }
                }
              }
            }
            if(!nowSolved) {
              return false;
            }
          }
          
        }
      }
      return nowSolved;
    }
  }
  
  //Loads grid with puzzle; each String of array
  //is of length 9 and contains a row of Sudoku puzzle
  public void loadPuzzle(String[] puzzle) {
    for(int i = 0; i < 9; i++) {
      for(int j = 0; j < 9; j++) {
        grid[i][j] = Character.getNumericValue(puzzle[i].charAt(j));
      }
    }
  }
  
  //puzzle is one long String with rows separated by whitespace
  //characters; loadPuzzle splits puzzle along whitespace
  //and then runs loadPuzzle(String[] puzzle)
  public void loadPuzzle(String puzzle) {
    String[] split = puzzle.split("\\s");
    loadPuzzle(split);
  }
  
  //Test client reads one long puzzle String from command line
  //and returns the solution and the time it took to solve in
  //millis
//  public static void main(String[] args) {
//      System.out.print("Please enter puzzle. Use '0' to indicate an unknown"
//      + " value and a space to indicate a new row: ");
//      //Example: 000009800 400108076 091760000 600240000 579000248 000087001 
//      //000051460 120806009 005400000
//      
//      Scanner scan = new Scanner(System.in);
//      String puzzle = scan.nextLine();
//      
//      SudokuSolver s = new SudokuSolver();
//      s.loadPuzzle(puzzle);
//      
//    long initial = System.currentTimeMillis();
//    s.solvePuzzle();
//    System.out.println("\nSolved in " +
//                            (System.currentTimeMillis() - initial) + " millis");
//    s.displayPuzzle();
//    
//  }
}
