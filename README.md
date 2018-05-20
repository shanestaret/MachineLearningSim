# Machine Learning Visualization
###### Official CIS112 (Data Struc. & Algorithm) Final Project by [Shane Staret](https://github.com/SStaret43).
###### This is a link to [my YouTube channel](https://www.youtube.com/channel/UCmQA16swmtPa29pRo9YtRTA?view_as=subscriber), featuring videos of this project on it. Here is a [video](https://www.youtube.com/watch?v=XPnZ38FEr74) that shows the program in action.
________________________________________________________________________________________________________________________________

### **Explanation & Design Process**

#### Essentially, this is an application that demonstrates how machine learning works. The objective is to "teach" the computer how to place the dots on the 3x3 grid in a specific manner. The specific way the we want the computer to put the dots is from the top left part of the grid, to the center left, to the bottom left, to the top center...to the bottom right. At first, the computer just randomly guesses where to place the dots. The "game" ends as soon as the computer places a dot on top of a space that already has a dot. As you continue to let the computer "play", it starts to learn from its past successful moves. So, if a move wasn't declared illegal, then for all successive attempts at the game, it will make that move since it is legal. The computer will always eventually place the dots in a specific order because I have the successful moves stored in a priority queue. This puts a "weight" on each grid space, and the computer learns to place a dot on each grid space from lowest weight to highest weight. The moves the computer makes each "game" are also documented and are stored within a queue. Once the game is over, the queue is unloaded and the moves are printed out in order.

#### I've always been extremely interested in machine learning, since the idea of acquiring knowledge seems so human to me. I always was puzzled by the idea of teaching an AI how to *learn*. I always thought, "*How could you possibly teach something how to learn?*" Thus, I decided to try it out for myself. I wanted to visually show how a computer may learn, which is the main reason why I chose to visualize the concept by showing how a computer may learn how to place dots on a grid in a specific order. For design, I realized that the UI would be rather simple, so I just drew what I wanted before actually creating the UI through JavaFX. Planning how the AI would actually learn was a bit confusing at first and it took me about an hour to decide how I actually wanted to implement it. I finally settled on using a priority queue to weight each grid space, so that the computer would eventually discover that it should place a dot on the grid in correspondance to the space's weight.
________________________________________________________________________________________________________________________________

### **Thoughts & Future Plans**

#### This project was created with the intent to show how machine learning works. Admittedly, at the moment this is a pretty dull example. Instead of showcasing just machine learning and how it works, I'd rather transform this into a "game" of sorts. Maybe change it up so that the computer learns how to play a game like checkers and then a human could actually play against it to see just how much it has learned. Overall, this project does a good job of showcasing machine learning, but its a bit boring. So ofcourse, I want to improve upon it. Here is a shortlist of what I will do in the future:
   * ##### add a delay when the computer is "playing" so that you can visually see the dots being placed
   * ##### add more grid spaces
   * ##### instead of a simulation that displays a computer "learning" how to place dots on a board, teach it how to play checkers
   * ##### create classes to house methods so code is more organized and readable
   * ##### iimprove formatting of UI and add CSS to UI to make it more professional and aesthetic
   * ##### *possibly* move UI to the web and allow other people to play against the computer that "learned"
