Game of Three    
--------------------------------------------------------------------------
The game build on Spring boot (v1.5.10.RELEASE), and WebSockets                                                 
                                                    
                  
Requirements
------------------------------------------------

1. Java 1.8.x
2. Maven 3.x.x

Steps to Run
------------------------------------------------

Download the GameThree-1.0.0.jar from Game-of-Three/GameThree/target/ to your local folder

from terminal type this:

java -jar GameThree-1.0.0.jar

The application starting, once you see this message on the console, that mean the application ready.

com.takeaway.game.Application            : Started Application in 5.067 seconds (JVM running for 5.709)



Steps to Start Play
------------------------------------------------


Open two Browser and hit this URL in the two browsers:
http://localhost:9090/

On the first browser Type your name and click join game.
On the second browser Type your name and click join the game.

The first player join the game has the ability to start the game by click start.

Once the player click start, random number should be generated and the game start.
Second play can click send in order to send the result number.

When one of the players win, and you want to play again just refresh the two browsers

