package com.company;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;


public class Game 
{
	private ArrayListStack<Integer> previousMoves;
	private int from = 0;
	private int to;
	private ArrayList<Boolean> occupied = new ArrayList<>();
	private ArrayList<ImageView> dots = new ArrayList<>();
	private int spaceToPlaceDot;
	private Driver driver;

	public Game()
	{
		driver = new Driver();
		play();
	}
	
	public void play()
	{
		previousMoves = new ArrayListStack<>();
		
		from = 0;  //initial state
		to = from;
		previousMoves.push(from);

		//setting "occupied" ArrayList to false because board is always blank to start; storing all dots within "dots" ArrayList
		for(int i = 0; i < 9; i++) {
			occupied.add(false);
			dots.add(new ImageView(new Image(getClass().getResourceAsStream("Black_Circle.png"))));
			dots.get(i).setFitHeight(100);
			dots.get(i).setFitWidth(100);
		}
		
		//continue to make moves until computer gets to final state,
        //storing each move along the way and recording whether it was legal or not  
		boolean gameDone = false;
		
		
	}  //end play
}