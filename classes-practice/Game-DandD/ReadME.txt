onsole D&D Mini-Game (Early Version)
This is a beginner-friendly console game inspired by Dungeons & Dragons mechanics.
It is the initial version created to practice Java basics, especially dependencies between classes and simple game loops.

What is this project about?
Two players create their own characters by choosing:

Name (validated so it has no digits or spaces)

Race (Orc, Human, Elf)

Class (Rogue, Paladin, Barbarian)

Players then roll a D20 dice to decide who goes first.

The battle begins:

Each player rolls a dice to attack

Attack power is calculated using dice roll + strength – (enemy armor + dexterity)

If the result is greater than 0, the opponent takes damage

If the result is less than or equal to 0, the attack misses

The game continues until one player’s health reaches 0.
The winner is announced at the end.

Why I made this
This is my practice project to:

Work with multiple classes (Character, CharacterClass)

Understand dependencies between objects

Practice loops, conditions, and basic game mechanics

Future Plans
I plan to expand the game with:

Weapon class (different damage types, bonuses, etc.)

Subclasses for races (e.g., High Elf, Dark Elf, etc.)

More combat mechanics (critical hits, healing, special skills)

Possibly a turn-based battle system with more detailed UI

How to play
Clone this repository and open it in IntelliJ IDEA (or any Java IDE)

Run Application.java

Follow the console instructions:

Enter your character name

Choose race and class

Roll the dice and fight

Notes
This is an early version, very simple and text-based

It is mainly for learning and experimenting, not a full game yet

Contributions and suggestions are welcome