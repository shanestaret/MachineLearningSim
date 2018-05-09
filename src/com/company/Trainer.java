package com.company;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Trainer extends Application {
    private Stage mainWindow; //the literal frame that pops up
    private Scene mainMenuUI, gameUI, explanationUI; //the different screens that we can get to within our "Stage" or frame
    private Button playButton, explanationButton, playAgainButton, gameReturnToMenuButton, explanationReturnToMenuButton; //buttons that user can interact with
    private Label explanation1 = new Label("Essentially, this program teaches the computer to put dots in a grid order without overlapping.");
    private Label explanation2 = new Label("It also does so in a specific order by the very end. It might take many games, but it learns as it goes.");
    private Label movesMade = new Label(""); //label that will hold the moves that were made by the computer when placing dots. "0" corresponds to the top left, "1" corresponds to the center left, "2" corresponds to the bottom left, "3" corresponds to the center top and so on.
    private ArrayUnboundedQueue<Integer> allMoves = new ArrayUnboundedQueue<>(); //queue that holds all previous moves for the game, no matter whether they were successful or not; print them out by FIFO standards
    
    //priority queue that holds all previous moves that were successful, causing the computer to always do those moves again;
    //the nature of the priority queue causes the computer to always put the dot on the screen in the same way after figuring out how to put all 9 dots in the individual grids without duplicates.
    private SortedABPriQ<Integer> previousSuccesses = new SortedABPriQ<>();
    
    //list that is a copy of the "previousSuccesses" priority queue;
    //used because we can figure out if a specific number (or move) is contained within a list versus a priority queue more easily
    private List<Integer> legal;
    
    //boolean list specifying whether a space on the grid is already occupied; an illegal move is to place a dot on a spot already occupied, so the game ends as soon as that occurs
    private ArrayList<Boolean> occupied = new ArrayList<>();
    
    private ArrayList<ImageView> dots = new ArrayList<>(); //ArrayList containing the actual images of the dots; need 9 instead of just 1 because otherwise they all couldn't appear at the same time
    
    private GridPane gridLayout = new GridPane(); //Grid where our dots will be put into and that will be shown in gameUI
    private int spaceToPlaceDot; //the spot that has been indicated as the place to put the next dot
    private int legalMovesMade; //the number of legal moves that the computer now KNOWS to make
    private int moveCount = 0; //the number of moves that have been made within a game so far; always default at 0
    private final int rows = 3; //the number of rows in our grid
    private final int columns = 3; //the number of columns in our grid
    private Node node; //node that will serve as a way to hold the gridlines node while the grid is cleared; this way the grid lines don't disappear when clicking "Play Again"

    //launches UI and program
    public static void main(String[] args) {
        launch(args);
    }

    //holds all UI and the logic when there are interactions made with the UI
    public void start (Stage primaryStage) {
        mainWindow = primaryStage; //the main window that pops up on the screen when the program is run

        //creating object that holds dimensions of user's screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        //giving title to our window
        mainWindow.setTitle("Dot Placement Training");

        //initializing buttons, giving them a label and dimensions
        //playButton brings user to gameUI and plays one "game"
        playButton = new Button("Play");
        playButton.setPrefWidth(500);
        playButton.setPrefHeight(100);

        //explanationButton brings user to explanationUI where the application is very briefly explained
        explanationButton = new Button("Explanation of Program");
        explanationButton.setPrefWidth(500);
        explanationButton.setPrefHeight(100);

        //playAgainButton that allows user to play another simulation after the computer failed to properly do it
        playAgainButton = new Button("Play Again");
        playAgainButton.setPrefWidth(150);
        playAgainButton.setPrefHeight(50);

        //gameReturnToMenuButton that literally returns the user to the mainMenuUI
        gameReturnToMenuButton = new Button("Return to Menu");
        gameReturnToMenuButton.setPrefWidth(150);
        gameReturnToMenuButton.setPrefHeight(50);

        //explanationReturnToMenuButton that literally returns the user to the mainMenuUI; cannot just be one button to return to main menu as it is needed in two separate layouts
        explanationReturnToMenuButton = new Button("Return to Menu");
        explanationReturnToMenuButton.setPrefWidth(100);

        //the layouts that will determine the formatting of every individual scene
        //mainMenuLayout for mainMenuUI will just have two buttons, one indicating that a user wants to run a simulation, while the other indicates that there is an explanation to the application
        HBox mainMenuLayout = new HBox(0);
        mainMenuLayout.getChildren().addAll(playButton, explanationButton);
        mainMenuLayout.setAlignment(Pos.CENTER);

        //gameOuterLayout that contains the grid where the simulation will take place (gridLayout),
        //the "Play Again" button and a "Return to Menu" button (rightLayout), and the "movesMade" label (gameBottomLayout)
        VBox gameOuterLayout = new VBox(20);
        HBox gameBottomLayout = new HBox(20);
        HBox gameTopLayout = new HBox(20);
        VBox rightLayout = new VBox(20);
        rightLayout.setAlignment(Pos.CENTER);
        rightLayout.getChildren().addAll(playAgainButton, gameReturnToMenuButton);
        gridLayout.setPrefSize(300, 300); //setting the size of the grid
        gridLayout.setGridLinesVisible(true); //showing the grid lines
        
        //setting the dimensions of each column
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / columns);
            gridLayout.getColumnConstraints().add(colConst);
        }
        
        //setting the dimensions of each row
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / rows);
            gridLayout.getRowConstraints().add(rowConst);
        }
        
        gameBottomLayout.getChildren().add(movesMade);
        gameBottomLayout.setAlignment(Pos.CENTER);
        gameTopLayout.getChildren().addAll(leftLayout, rightLayout);
        gameTopLayout.setAlignment(Pos.CENTER);
        gameOuterLayout.getChildren().addAll(gameTopLayout, gameBottomLayout);
        gameOuterLayout.setAlignment(Pos.CENTER);
        gameOuterLayout.setPadding(new Insets(20, 20, 20, 20));

        //explanationLayout for explanationUI which holds the three labels that give a very brief overview of the application
        VBox explanationLayout = new VBox(20);
        explanationLayout.getChildren().addAll(explanation1, explanation2, explanationReturnToMenuButton);
        explanationLayout.setAlignment(Pos.CENTER);
        explanationLayout.setPadding(new Insets(20, 20, 20, 20));

        //the scenes that will display within our window
        mainMenuUI = new Scene(mainMenuLayout);
        gameUI = new Scene(gameOuterLayout);
        explanationUI = new Scene(explanationLayout);

        //gets rid of whitespace and does not allow user to resize window
        mainWindow.sizeToScene();
        mainWindow.setResizable(false);

        //shows the main menu as soon as the program is ran, so the mainMenuUI is the first thing seen
        mainWindow.setScene(mainMenuUI);
        mainWindow.show();


        //centers window on user's screen
        mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        //when playButton is clicked, switch to gameUI and play one game
        playButton.setOnAction(e -> {
            mainWindow.setScene(gameUI);
            mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
            play();
        });

        //when playAgainButton is clicked, play another game
        playAgainButton.setOnAction(e -> {
            play();
        });

        //when explanationButton is clicked, switch to explanationUI
        explanationButton.setOnAction(e -> {
            mainWindow.setScene(explanationUI);
            mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });

        //when gameReturnMenuButton is clicked, switch to mainMenuUI
        gameReturnToMenuButton.setOnAction(e -> {
            mainWindow.setScene(mainMenuUI);
            mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });

        //when explanationReturnMenuButton is clicked, switch to mainMenuUI
        explanationReturnToMenuButton.setOnAction(e -> {
            mainWindow.setScene(mainMenuUI);
            mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });
    }

    //the method that actually starts and stops the simulation
    public void play() {
        //clear out occupied so that nothing remains from last game
        occupied.clear();
        
        //setting "occupied" ArrayList to false because board is always blank to start; storing all dots within "dots" ArrayList
        for(int i = 0; i < 9; i++) {
            occupied.add(false);
            dots.add(new ImageView(new Image(getClass().getResourceAsStream("Black_Circle.png"))));
            dots.get(i).setFitHeight(100);
            dots.get(i).setFitWidth(100);
        }
        
        movesMade.setText("Moves made: ");
        node = gridLayout.getChildren().get(0);
        gridLayout.getChildren().clear(); //clearing the grid to set up a new simulation
        gridLayout.getChildren().add(0, node);
        spaceToPlaceDot = 0; //we always start with placing the dot at spot 0
        
        //if the space we want to place the dot is not already occupied...
        while(occupied.contains(false)) {
            legalMovesMade = 0; //legalMovesMade is automatically reset to 0 because the simulation is just beginning
            moveCount++; //moveCount increments every time a move is going to be made
            
            
            legal = previousSuccesses.returnQueue(); //setting legal List identical to previousSuccesses priority queue so we can more easily figure out what is actually in the priority queue already and print its contents out
            
            //loops through the nine elements within legal
            for (int i = 0; i < 9; i++) {
                //if legal contains a grid number equal to "i"...
                if(legal.contains(i)) {
                    //if that specific grid number in legal is not already occupied...
                    if(!occupied.get(i)) {
                        setGridLayout(i); //add dot at that location
                        allMoves.enqueue(i); //add the move into the allMoves queue
                        occupied.set(i, true); //set the occupied status of that location to true
                        moveCount++; //increment the counter
                    }
                    legalMovesMade++; //increment the number of legalMovesMade
                    
                    //if every move made was legal, then the grid has been filled
                    if (legalMovesMade == 9 && legal.size() > 1) {
                        movesMade.setText("");
                        movesMade.setText("Success! Exit and re-run to try again!");
                        break;
                    }
                }
            }
            
            //if this is not the first move in the game; needed because we always want the very first dot to go to 0
            if(moveCount != 1) {
                spaceToPlaceDot = (int) (Math.random() * 9); //choose a random number from 0 to 8 which will correspond to the grid spot
            }
            //if this spot is occupied already...
            if(occupied.get(spaceToPlaceDot)) {
                allMoves.enqueue(spaceToPlaceDot); //store that move
                break; //end the game because an illegal move was just made
            }
            //if the spot indicates grid number 0...
            else if(spaceToPlaceDot == 0 && occupied.get(0) == false) {
                setGridLayout(spaceToPlaceDot); //place dot at grid number 0
                
                //if the spot was placed first (which it should be since 0 is always placed first) and this was not already indicated as a legal move, then add it to the previousSuccesses priority queue
                if(spaceToPlaceDot == moveCount - 1 && !previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            //this same logic applies for all else if statements below
            
            else if(spaceToPlaceDot == 1 && occupied.get(1) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 2 && occupied.get(2) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 3 && occupied.get(3) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 4 && occupied.get(4) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 5 && occupied.get(5) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 6 && occupied.get(6) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 7 && occupied.get(7) == false) {
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            else if(spaceToPlaceDot == 8 && occupied.get(8) == false){
                setGridLayout(spaceToPlaceDot);
                if(!previousSuccesses.toString().contains(Integer.toString(spaceToPlaceDot))) {
                    addToSuccess();
                }
            }
            occupied.set(spaceToPlaceDot, true); //set the indicated spot as occupied now
            allMoves.enqueue(spaceToPlaceDot); //enqueue the most recent spot that a dot was put on
        }
        printMoves(); //print the moves out within the gameUI
    }

    //prints the moves out within the gameUI
    public void printMoves() {
        //for every move made so far...
        for(int i = 0; i < moveCount; i++){
            if(legalMovesMade != 9) //so long as they aren't all legal moves
            movesMade.setText(movesMade.getText() + allMoves.dequeue() + ", "); //print out the move
        }
        moveCount = 0; //set the moves to 0 because the game is over
        System.out.println(previousSuccesses); //print out all legal moves made that the computer will now remember
    }

    //method that adds a legal move to the priority queue
    public void addToSuccess() {
        previousSuccesses.enqueue(spaceToPlaceDot);
    }

    //method that places a dot on a grid space depending on what location was chosen
    public void setGridLayout(int n) {
        if(n == 0)
            gridLayout.add(dots.get(0), 0, 0);
        else if(n == 1)
            gridLayout.add(dots.get(1), 0, 1);
        else if(n == 2)
            gridLayout.add(dots.get(2), 0, 2);
        else if(n == 3)
            gridLayout.add(dots.get(3), 1, 0);
        else if(n == 4)
            gridLayout.add(dots.get(4), 1, 1);
        else if(n == 5)
            gridLayout.add(dots.get(5), 1, 2);
        else if(n == 6)
            gridLayout.add(dots.get(6), 2, 0);
        else if(n == 7)
            gridLayout.add(dots.get(7), 2, 1);
        else if(n == 8)
            gridLayout.add(dots.get(8), 2, 2);

    }
}
