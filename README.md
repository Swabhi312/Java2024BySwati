
## Requirements ##

- JDK >= 1.8
- Maven or Gradle
- feel free to choose any libraries for serialize/deserialize JSON and testing
- not recommended to add any additional libraries/frameworks, like spring or other high level frameworks

## Description ##

Problem statement: You need to build a scratch game, that will generate a matrix (for example 3x3) from symbols(based on probabilities for each individual cell) and based on winning combintations user either will win or lost.
User will place a bet with any amount which we call *betting amount* 

## Steps to execute this Game ##
Take checkout of main branch in local like below screenshot

Build should be success with maven install.

![Build_Success.png](..%2FUsers%2Fabhilad1%2FPictures%2FBuild_Success.png)
After Build, target folder is created with  game_design-0.0.1-jar-with-dependencies.jar 

execute command from target folder:
java -jar game_design-0.0.1-jar-with-dependencies.jar config.json 100

Test cases executed in below screenshot.
![](C:\Users\abhilad1\Pictures\win_and_lost_cases.png)