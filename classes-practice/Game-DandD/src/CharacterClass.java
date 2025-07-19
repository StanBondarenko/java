public class CharacterClass {
    private String name;
    private int armor;
    private int health;
    private int strength;
    private int dexterity;
//******************Constructor

    public CharacterClass(String name, int armor, int health, int strength, int dexterity) {
        this.name = name;
        this.armor = armor;
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
    }

//******************Getters

    public String getName() {
        return name;
    }

    public int getArmor() {
        return armor;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }
//*************************Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
}
