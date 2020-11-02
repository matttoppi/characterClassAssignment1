package characterCreation ;

import java.util.HashMap;
import java.util.Map;

public class Character {
    /* demographics
     * charClass = can only equal classes listed in <placeHolder>
     * race = can only equal "elf" "dwarf" "human" "gnome" "halfling"
     */

    private String name = "";
    private String charClass = "";
    private String race = "";

    /*Stats will hold 8 main game stats
     * Strength, dexterity, constitution, Intelligence, Wisdom, Charisma,
     * maxHP, maxMP
     * CurrentHP, Current MP
     * Bonuses will hold our 6 bonus stats (all but HP & MP)

     */

    private final Map<String, Integer> stats = new HashMap<>();
    private final Map<String, Integer> bonuses = new HashMap<>();

    public Character(String name, String race, String charClass) {
        /*create a character given name, race, and class
         *
         * Load base stats for all starting characters into the stats
         * Create unique bonues for a later calculation
         * Call <add> to calculate bonus stats
         *
         * @param name Character Name
         * @param race Character Race
         * @param class Character job class (see determineBonuses)
         */

        this.name = name;
        this.race = race.toLowerCase();
        this.charClass = charClass.toLowerCase();

        stats.put("str", 2);
        stats.put("dex", 2);
        stats.put("con", 3);
        stats.put("wis", 3);
        stats.put("int", 2);
        stats.put("cha", 2);

        //test

        bonuses.put("str", 0);
        bonuses.put("dex", 0);
        bonuses.put("con", 0);
        bonuses.put("wis", 0);
        bonuses.put("int", 0);
        bonuses.put("cha", 0);

        determineBonuses();
        buildCharacter(1);
        stats.put("HP", stats.get("maxHP"));
        stats.put("MP", stats.get("maxMP"));
    }

    // changeHP
    public void takeDamage(int damage){
        stats.put("HP", stats.get("HP") - 100);
        if (stats.get("HP") <= 0){
            printGameOver();
            //calls the printGameOver method if the player no longer has any health
        }
    }

    public void getHealed(int heal) {
        stats.put("HP", stats.get("HP") + 100);
        if (stats.get("HP") > stats.get("maxHP")){
            stats.put("HP", stats.get("maxHP"));
            //this is a check to ensure player does not go over their HP cap
        }
    }

    public void printGameOver(){
        System.out.println("Game over.");
        System.exit(0);
    }
    //printGameOver if 0 HP

    private void determineBonuses(){
        /**
         * Determines bonuses based on class and race;
         * Also holds main classes (only class choices) for map
         */

        //first bonus based on class
        switch (charClass){
            case "cleric":
                bonuses.put("wis", bonuses.get("wis")+2);
                bonuses.put("wis", bonuses.get("cha")+1);
                bonuses.put("wis", bonuses.get("dex")-1);
                break;
            case "barbarian":
                bonuses.put("str", bonuses.get("str")+3);
                bonuses.put("dex", bonuses.get("dex")+2);
                bonuses.put("int", bonuses.get("int")-2);
                bonuses.put("wis", bonuses.get("wis")-1);
                break;
            //add wizard & ranger here (no default)
        }
        // now to add race bonuses
        switch (race) {
            case "half-ling": case "hobbit": case "halfling":
                bonuses.put("dex", bonuses.get("dex")+3);
                bonuses.put("cha", bonuses.get("cha")+2);
                bonuses.put("str", bonuses.get("str")-2);
                break;
            case "dwarf": case "duegar":
                bonuses.put("str", bonuses.get("str")+3);
                bonuses.put("con", bonuses.get("con")+3);
                bonuses.put("int", bonuses.get("int")-2);
                bonuses.put("cha", bonuses.get("cha")-3);
                break;
            //add elf or gnome by default
        }
    }

    private void buildCharacter(int level){
        /**build character using bonuses and level
         *
         * @param level character being built from
         */
        for (String key : bonuses.keySet()){
            stats.put(key, level * (stats.get(key)+ bonuses.get(key)));
        }

        //now calculate our HP and MP using con and wis (if -1 or less treat as 0)
        if (stats.get("con")>0) {
            stats.put("maxHP", stats.get("con") * 100);
        }else{
            stats.put("maxHP", 100);
        }

        //mp now
        if(stats.get("wis")>1){
            stats.put("maxMP", stats.get("wis")*10);
        }else{
            stats.put("maxMP", 10);
        }

    }



    @Override
    public String toString() {
        /** Inherits properties of toString
         * & Overrides it to print our template
         * @return desc Description of character based on printf template
         */
        String desc = String.format("%s the %s %s with ", name, race, charClass);

        for (String key : stats.keySet()) {
            if (!"maxHP".equals(key) && !"maxMP".equals(key)) {
                desc += String.format("%s of %d ", key, stats.get(key));
            }
        }
        return desc;
    }
}

