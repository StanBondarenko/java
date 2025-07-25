import java.util.Random;
import java.util.Scanner;

public class Application {

    public static void main(String[] arg) {

        // Scanner for reading user input from console
        Scanner scr = new Scanner(System.in);

        // Available races
        String[] races = {"Orc", "Human", "Elf"};

        boolean isDone = true;

        // ***************** Create two players and prepare classes
        Character gamer1 = new Character();
        Character gamer2 = new Character();

        // Predefined character classes (className, armor, health, strength, dexterity)
        CharacterClass rogue = new CharacterClass("Rogue", 3, 50, 1, 8);
        CharacterClass paladin = new CharacterClass("Paladin", 10, 70, 3, 0);
        CharacterClass barbarian = new CharacterClass("Barbarian", 6, 60, 2, 4);

        // Random generator for dice rolls
        Random random = new Random();

        // ========================== PLAYER SETUP PHASE ==========================
        while (isDone) {
            // Loop for both players
            for (int i = 1; i <= 2; i++) {

                // === Step 1: Player name ===
                System.out.println("🎮 Player " + i + ", enter your character name:");
                String userPut = scr.nextLine();

                // Validate name: no digits, no spaces, not empty
                if (userPut.isEmpty() || containsDigit(userPut) || containsGaps(userPut)) {
                    System.out.println("❌ Name cannot contain numbers, spaces, or be empty. Try again!");
                    i--; // repeat input for same player
                    continue;
                }

                // Save name depending on which player is entering it
                switch (i) {
                    case 1 -> gamer1.setName(userPut);
                    case 2 -> gamer2.setName(userPut);
                }

                // === Step 2: Choose race ===
                System.out.println("🧬 " + (i == 1 ? gamer1.getName() : gamer2.getName()) +
                        ", choose your race --> [1: 🐗 Orc, 2: 🤴 Human, 3: 🧝 Elf]");
                userPut = scr.nextLine();

                // Validate that input is a number, not letters
                if (containsLetter(userPut)) {
                    System.out.println("⚠️ Please enter a number from the list!");
                    i--;
                    continue;
                }

                // Inform the player about chosen race
                switch (Integer.parseInt(userPut)) {
                    case 1 -> System.out.println("✅ You chose the mighty Orc! 🐗💪");
                    case 2 -> System.out.println("✅ You chose the brave Human! 🤴✨");
                    case 3 -> System.out.println("✅ You chose the wise Elf! 🧝‍♂️🌿");
                    default -> {
                        System.out.println("⚠️ No such race in the list! Try again.");
                        i--;
                        continue;
                    }
                }

                // Save race to the correct player
                switch (i) {
                    case 1 -> gamer1.setRace(races[Integer.parseInt(userPut) - 1]);
                    case 2 -> gamer2.setRace(races[Integer.parseInt(userPut) - 1]);
                }

                // === Step 3: Choose class ===
                System.out.println("🛡️ " + (i == 1 ? gamer1.getName() : gamer2.getName()) +
                        ", your race is " + (i == 1 ? gamer1.getRace() : gamer2.getRace()) +
                        "\nNow choose your class:" +
                        "\n1️⃣ Rogue (⚔️ Armor 3, ❤️ Health 50, 💪 Strength 1, 🤸 Dexterity 8)" +
                        "\n2️⃣ Paladin (🛡️ Armor 10, ❤️ Health 70, 💪 Strength 3, 🤸 Dexterity 0)" +
                        "\n3️⃣ Barbarian (🥊 Armor 6, ❤️ Health 60, 💪 Strength 2, 🤸 Dexterity 4)");

                userPut = scr.nextLine();

                // Validate that input is a number, not letters
                if (containsLetter(userPut)) {
                    System.out.println("⚠️ Please enter a number from the list!");
                    i--;
                    continue;
                }

                // Assign chosen class
                if (i == 1) {
                    switch (userPut) {
                        case "1" -> gamer1.setKlass(rogue);
                        case "2" -> gamer1.setKlass(paladin);
                        case "3" -> gamer1.setKlass(barbarian);
                        default -> System.out.println("⚠️ Choose from the list!");
                    }
                } else {
                    switch (userPut) {
                        case "1" -> gamer2.setKlass(rogue);
                        case "2" -> gamer2.setKlass(paladin);
                        case "3" -> gamer2.setKlass(barbarian);
                        default -> System.out.println("⚠️ Choose from the list!");
                    }
                }

                // After second player setup, end the setup phase
                if (i == 2) {
                    isDone = false;
                }
            }
        }

        // ========================== WHO GOES FIRST ==========================
        isDone = true;
        while (isDone) {
            System.out.println("\n🎲 Let's start the game!\nFirst, let's decide who goes first...");

            int d20;

            // Player 1 rolls
            System.out.println("\n🎲 " + gamer1.getKlass().getName() + " " + gamer1.getName() +
                    ", press ENTER to roll the dice! 🎲");
            scr.nextLine();
            d20 = random.nextInt(20) + 1; // roll from 1 to 20
            System.out.println("🎯 " + gamer1.getName() + " rolled: " + d20);
            int gamer1D20 = d20;

            // Player 2 rolls
            System.out.println("\n🎲 " + gamer2.getKlass().getName() + " " + gamer2.getName() +
                    ", press ENTER to roll the dice! 🎲");
            scr.nextLine();
            d20 = random.nextInt(20) + 1;
            System.out.println("🎯 " + gamer2.getName() + " rolled: " + d20);
            int gamer2D20 = d20;

            // If tie, repeat the process
            if (gamer1D20 == gamer2D20) {
                System.out.println("🤝 It's a tie! Let's roll again!");
                continue;
            }
            // Use Method Who goes first
            whoGoesFirst(gamer1,gamer2,gamer1D20,gamer2D20);

            System.out.println("\n🏆 The bravest is —> " +
                    (gamer1D20 > gamer2D20 ? gamer1.getKlass().getName() + " " + gamer1.getName()
                            : gamer2.getKlass().getName() + " " + gamer2.getName()) +
                    "\nThey rolled the highest number and go FIRST! 🚀\nPress ENTER to continue...");
            scr.nextLine();
            isDone = false;
        }

        // ========================== BATTLE START ==========================
        System.out.println("\n🔥 ==================== BATTLE BEGINS ==================== 🔥\n");

        // Battle loop: continues until one player's health <= 0
        while (gamer1.getKlass().getHealth() > 0 && gamer2.getKlass().getHealth() > 0) {

            if (gamer1.isGoFirst()) {
                // === Player 1 attacks ===
                System.out.println("🎲 " + gamer1.getKlass().getName() + " " + gamer1.getName() +
                        " attacks " + gamer2.getName() + "! ⚔️ Press ENTER to roll the dice...");
                scr.nextLine();
                int d20 = random.nextInt(20) + 1;
                System.out.println("🎯 " + gamer1.getName() + " rolled: " + d20);

                // Calculate hit power (roll + strength - (enemy armor + dexterity))
                countDamage(gamer1, gamer2, d20, scr);
                // Check if player2 is dead
                if (gamer2.getKlass().getHealth() <= 0) {
                    System.out.println("🏆 WINNER!!! 🎉🎉🎉 The winner is -> " +
                            gamer1.getName() + " 🏅\nPress ENTER to exit");
                    scr.nextLine();
                    break;
                }

            } else {
                // === Player 2 attacks ===
                System.out.println("🎲 " + gamer2.getKlass().getName() + " " + gamer2.getName() +
                        " attacks " + gamer1.getName() + "! ⚔️ Press ENTER to roll the dice...");
                scr.nextLine();
                int d20 = random.nextInt(20) + 1;
                System.out.println("🎯 " + gamer2.getName() + " rolled: " + d20);
                    countDamage(gamer2,gamer1,d20,scr);
                    if (gamer1.getKlass().getHealth() <= 0) {
                        System.out.println("🏆 WINNER!!! 🎉🎉🎉 The winner is -> " +
                                gamer2.getName() + " 🏅\nPress ENTER to exit");
                        scr.nextLine();
                        break;
                    }
            }
        }
    }


    // ========================== UTILITY METHODS ==========================

    // Check if string contains a digit
    public static boolean containsDigit(String text) {
        return text.matches(".*\\d.*");
    }

    // Check if string contains letters (for validation of numeric inputs)
    public static boolean containsLetter(String text) {
        return text.matches(".*[A-Za-zА-Яа-яЁё].*");
    }

    // Check if string contains gaps (spaces)
    public static boolean containsGaps(String text) {
        return text.matches("\\s.*");
    }
    // Count damage
    public static void countDamage(Character attaker, Character defender, int diceRoll,Scanner scr){
        int powerOfHit = (diceRoll +attaker.getKlass().getStrength()) -
                (defender.getKlass().getArmor() + defender.getKlass().getDexterity());
        if (powerOfHit > 0) {
            System.out.println("💥 HIT! " +defender.getName() + " takes " + powerOfHit + " damage!");
            defender.getKlass().setHealth(defender.getKlass().getHealth() - powerOfHit);

            System.out.println("❤️ " + defender.getName() + " now has " +
                    defender.getKlass().getHealth() + " HP\n👉 Press ENTER to continue...");
            attaker.setGoFirst(false);
            scr.nextLine();
        } else {
            System.out.println("❌ Miss! " + defender.getName() + " takes 0 damage.");
            System.out.println("❤️ " + defender.getName() + " still has " +
                    defender.getKlass().getHealth() + " HP\n👉 Press ENTER to continue...");
            attaker.setGoFirst(false);
            scr.nextLine();
        }
    }
    public static void whoGoesFirst(Character g1,Character g2,int roll1, int roll2){
        // Decide who goes first
        if (roll1 > roll2) {
            g1.setGoFirst(true);
        } else {
            g2.setGoFirst(true);
        }

    }

}
