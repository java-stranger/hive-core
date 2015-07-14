# hive-core
The core classes for a "Hive" board game implementation.

## Goal
The idea of the project arose when I played Hive (https://en.wikipedia.org/wiki/Hive_(game)) with a friend.
As he was constantly beating me, I was wondering how hard it could be to program an AI (to play instead of me :-P).
I never programmed any games in my life, and this is my first experience with Java. 

So the whole thing is just for fun and learning!

## AI contest
A possible outcome would be an API and a set of tools allowing to quickly develop a bot. 
From there, it would be only a short step forward to build a platform allowing to compare different bots.
And why not a bot Hive tournament! :)

## What's inside
Includes classes for modeling the playing board (position), pieces with possible moves (based on the position).
Can be used to build your Hive AI.

Also contains a "view" which is meant to be an abstraction layer between 
the core functionality (position, players, moves), and the GUI renderer (https://github.com/java-stranger/hive-playN).
Probably will be moved to another repo in future.

## Compiling
You need Maven to compile and install it:
```
mvn compile
mvn install
```
You need to install so Maven can find the package when you will compile GUI (https://github.com/java-stranger/hive-playN)
