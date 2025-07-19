import java.util.Map;
import java.util.Objects;

public  class Character {
    private String name;
    private String race;
    private CharacterClass klass;
    private boolean isGoFirst;
    //private Map<String,Integer> weapon;
//********************Constructor
    public Character() {

    }
//*******************Getters
    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public CharacterClass getKlass() {
        return klass;
    }
    public boolean isGoFirst(){
        return isGoFirst;
    }

//    public Map<String, Integer> getWeapon() {
//        return weapon;
//    }
//******************Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setKlass(CharacterClass klass) {
        this.klass = klass;
    }
    public void setGoFirst(boolean isGoFirst){
        this.isGoFirst=isGoFirst;
    }

}
