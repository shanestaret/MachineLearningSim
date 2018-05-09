/*package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Trainer {
    String initialPlace = "HTH"; //initial placement of coins
    String advancedPlace; //placement of coins after flip
    char coinSide;  //head or tails
    boolean illegal = false;
    private ArrayListStack<String> previousMoves = new ArrayListStack<>();
    int coinPlace; //coin that will be flipped
    boolean legalFlip, finished; //legalFlip is true if move was legal; finished is true if computer finished the game

    public void train() {
        {

            initialPlace = from;
            previousMoves.push(from);

            //continue to make moves until computer gets to final state of THT,
            //storing each move along the way and recording whether it was legal or not
            finished = false;
            while (finished == false) {
                // randomly select coin to flip
                coinPlace = (int) (Math.random() * 3);  //will give me 0, 1, or 2
                from = previousMoves.top();
                coinChar = from.charAt(coinPlace);
            }


        }  //end play
    }

}
*/