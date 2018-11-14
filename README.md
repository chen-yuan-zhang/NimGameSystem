# Nim Game System
Application of [**Sprague–Grundy theorem(SG theorem)**](https://en.wikipedia.org/wiki/Sprague%E2%80%93Grundy_theorem)

## Introduction
The project is the second assignment of Programming and Software Development(COMP90041) Semester 1, 2018. 
The main challenge of the project is how to implement a **AI with victory guaranteed strategy in advanced Nim Game**.
Here I will briefly introduce how I apply SG theorm on this problem.

## Advanced Nim Game
There are multiple variants of [Nim Game](https://en.wikipedia.org/wiki/Nim).
In this project, the advanced Nim Game is defined as following(cited from the [instruction document](https://github.com/zcy921716/NimGameSystem/blob/master/projectC(1).pdf)):
>The rules of the advanced Nim game are as follows. The major rule in this advanced Nim game is that a player is only allowed to remove 1 stone or 2 adjacent stones in each move. Here, adjacent stones are stones who are neighbors to each other. Hence, the upper bound of stones to be removed is always 2. However, the requirement on the adjacent stones makes the position of the stones matters, i.e., removing the same number of stones at diﬀerent positions may produce diﬀerent game states, while in the original Nim game stones are conceptually the same, i.e., removing the same number of stones always produces the same game states. For example, given 5 stones represented as < ∗∗∗∗∗ >, 
>*  in the original Nim game removing 2 stones will always produce the state < ∗∗∗ >
>*  in the advanced Nim game, depending on which stones are removed, removing 2 stones produces multiple states. In the following example, note that state 2 diﬀers from state 3 because in state 2 the ﬁrst two remained stones cannot be removed in a single move since they are not adjacent while in state 3 the ﬁrst two remained stones can be removed in a single move.
>  1. removing the ﬁrst and the second, results to < ××∗∗∗ >
>  2. removing the second the third, results to < ∗××∗∗ >
>  3. removing the third and the fourth, results to < ∗∗××∗ > 
>  4. removing the fourth and the ﬁfth, results to < ∗∗∗×× >
>
>Similar to the original Nim game, each player takes turns to remove either one stone or two adjacent stones. A player wins if he / she removed the last stone.

## Game Strutuce
The advanced Nim Game has only one paremeter: the number of stones. we call an advanced game with _n_ stones _AdvanceNim(n)_.
So our goal is to find the win strategy of _AdvanceNim(n)_.

We can easily find that if a move is make on _AdvanceNim(n)_, the game will become the sum game of _AdvanceNim(x)_ and _AdvanceNim(y)_ which satisfy
```
x+y = n-1(if the move removed 1 stone)
```
or 
```
x+y = n-2(if the move removed 2 stones)
```
According to [the property of SG function](https://en.wikipedia.org/wiki/Sprague%E2%80%93Grundy_theorem#Combining_Games), 
namely a state _s_ with two sub games _AdvanceNim(a)_ and _AdvanceNim(b)_,
```
SG(s) = SG(AdvanceNim(a)) xor SG(AdvanceNim(b))
```
we can derive 
```
SG(AdvanceNim(n)) = mex(SG(AdvanceNim(x)) xor SG(AdvanNim(y))|for all non-negative pairs(x,y) that x+y=n-1 or x+y=n-2)
```
(You can find the definition of xor [here](https://en.wikipedia.org/wiki/Exclusive_or) and mex [here](https://en.wikipedia.org/wiki/Mex_(mathematics)).)

According to the equation above, we can calculate the SG function of the current state recursively and judge if a win-guaranteed strategy exists in this state. 
Of course, to improve the efficency, we can first calculate a table of _SG(n)_ and then just use the value accordingly.

For example, if now there are three sub-games _AdvanceNim(4)_, _AdvanceNim(6)_ and _AdvanceNim(3)_, the SG function of the state is
```
SG(*) = SG(AdvanceNim(4)) xor SG(AdvanceNim(6)) xor SG(AdvanceNim(3))
```
If the SG function is zero then there is no win-guaranteed strategy while if SG function is non-zero there should exist a win-guaranteed strategy.
The next move can be found by traversing all successors and those moves make the SG function of successor zero can be picked.

The JAVA implementation of above algorithm can be found [here](https://github.com/zcy921716/NimGameSystem/blob/master/SG.java).
