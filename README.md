# ogafit-game-client
Ogafit-game-client is a java websocket client library to communicate with ogafit-game-server


## Oga-fit Game

* When a key is pressed in server (instruction), the client receives it; and has a timer
counting up to X seconds in which the same key has to be pressed.
* The server verifies if the keypress sent by client matched “the instruction” and assign +1
point on correct, -1 on a wrong key and 0 for timeouts.
* The game is over when the score reaches either +10 points or -3 points.
* The game is also over if the client does not respond for 3 continuous instructions.
* Each score update should send the score back to the connected client.
* The client should also know the timeout value for each instruction received.


## Define Environment varibles
Below are the environment variables to be configured to start the web client


| Variable                    | Description                                            | Default (if present) |
| --------------------------- | ------------------------------------------------------ | -------------------- |
| OGA_FIT_GAMESERVER_URL      | URL to communicate with Ogafit-game-server  |  ws://localhost:8080            |




## Improvements

* Instruction Expiry logic sits in Ogafit-game-server, needs to be handled by Client

## Available gradle options

* ` gradle clean build` : To build the client library
* ` gradle bootRun`     : To run the client libray
 
