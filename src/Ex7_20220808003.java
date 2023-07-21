import java.util.*;

public class Ex7_20220808003 {
    public static void main(String[] args) throws Exception {
        Party party = new Party();
        Party party2 = new Party();
        Paladin pal1 = new Paladin("test 1");
        Paladin pal2 = new Paladin("test 4");
        Paladin pal3 = new Paladin("test 5");
        Cleric cl2 = new Cleric("test 6");
        Cleric cl1 = new Cleric("test 2");
        Cleric cl4 = new Cleric("test 4");
        Cleric cl5 = new Cleric("test 4");
        Cleric cl6 = new Cleric("test 4");
        Cleric cl7 = new Cleric("test 4");
        Cleric cl8 = new Cleric("test 4");
        Cleric cl9 = new Cleric("test 4");
        Cleric cl10 = new Cleric("test 4");

        cl5.joinParty(party);

        Warrior war1 = new Warrior("test 3");
        party.partyLevelUp();
        pal1.levelUp();
        pal1.joinParty(party);
        cl1.joinParty(party);
        war1.joinParty(party);
        pal2.joinParty(party);
        pal3.joinParty(party);
        cl2.joinParty(party);
        party.partyLevelUp();

        System.out.println(party);
        cl2.quitParty();
        System.out.println(party);

        Weapon weapon = new Weapon("Tuna", 5, 10);
        war1.lootWeapon(weapon);
        System.out.println(pal1.health);
        System.out.println(cl1.health);
        war1.attack(cl1);
        war1.attack(pal1);
        System.out.println(pal1.health);
        System.out.println(cl1.health);
    }
}
interface Damageable {
    void takeDamage(int damage) throws CharacterIsNotInPartyException;
    void takeHealing(int healing);
    boolean isAlive();
}

interface Caster {
    void castSpell(Damageable target) throws CharacterIsNotInPartyException;
    void learnSpell(Spell spell);
}

interface Combat extends Damageable {
    void attack(Damageable target) throws CharacterIsNotInPartyException;
    void lootWeapon(Weapon weapon);
}

interface Useable {
    int use();
}


class Spell implements Useable{
private int minHeal;
private int maxHeal;
private String name;

public Spell(String name, int minHeal, int maxHeal){
    this.name = name;
    this.minHeal = minHeal;
    this.maxHeal = maxHeal;
}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private int heal() {
        Random rand = new Random();
        return rand.nextInt(this.maxHeal - this.minHeal + 1) + this.minHeal;
    }
    public int use() {
        return heal();
    }
}


class Weapon implements Useable {
    private int minDamage;
    private int maxDamage;
    private String name;

    public Weapon(String name, int minDamage, int maxDamage) {
        this.name = name;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int attack() {
        Random rand = new Random();
        return rand.nextInt(this.maxDamage - this.minDamage + 1) + this.minDamage;
    }

    public int use() {
        return attack();
    }
}


    class Attributes {
        private int strength;
        private int intelligence;

        public Attributes() {
            this.strength = 3;
            this.intelligence = 3;
        }

        public Attributes(int strength, int intelligence) {
            this.strength = strength;
            this.intelligence = intelligence;
        }

        public void increaseStrength(int amount) {
            this.strength += amount;
        }

        public void increaseIntelligence(int amount) {
            this.intelligence += amount;
        }

        public int getStrength() {
            return this.strength;
        }

        public int getIntelligence() {
            return this.intelligence;
        }

        public String toString() {
            return "Attributes [Strength=" + this.strength + ", intelligence=" + this.intelligence + "]";
        }
    }


    abstract class Character implements Comparable<Character> {
        private String name;
        protected int level;
        protected Attributes attributes;
        protected int health;

        public Character(String name, Attributes attributes) {
            this.name = name;
            this.attributes = attributes;

        }
        public Attributes getAttributes() {
            return this.attributes;
        }

        public String getName() {
            return this.name;
        }

        public int getLevel() {
            return this.level;
        }

        public abstract void levelUp();

        public int compareTo(Character other) {
            if (this.level > other.level)
                return 1;
            else if (this.level == other.level)
                return 0;
            else
                return -1;
        }

        public String toString() {
            return getClass().getSimpleName() + " LvL: " + this.level + " - " + this.attributes;
        }
    }


    abstract class PlayableCharacter extends Character implements Damageable {
        private boolean inParty;
        private Party party;

        public PlayableCharacter(String name, Attributes attributes) {
            super(name, attributes);
        }

        public boolean isInParty() {
            return inParty;
        }

        public void joinParty(Party party) throws PartyLimitReachedException, AlreadyInPartyException {
            try {
                party.addCharacter(this);
                this.inParty = true;
                this.party = party;
            }catch(AlreadyInPartyException E){
                System.out.println(E);
            }catch(PartyLimitReachedException E){
                System.out.println(E);
            }
        }

        public void quitParty() throws CharacterIsNotInPartyException {
            try{
                party.removeCharacter(this);
                this.inParty = false;
                this.party = null;

            } catch(CharacterIsNotInPartyException excp) {
                System.out.println("Cannot quit party");
            }
        }
    }


        abstract class NonPlayableCharacter extends Character {
            public NonPlayableCharacter(String name, Attributes attributes) {
                super(name, attributes);
            }
        }


        class Merchant extends NonPlayableCharacter {
            public Merchant(String name) {
                super(name, new Attributes(0,0));
            }

            public void levelUp() {
                // bro's a merchant he ain't levelin' up
            }
        }


        class Skeleton extends NonPlayableCharacter implements Combat {
            private Weapon weapon;

            public Skeleton(String name, Attributes attributes) {
                super(name, attributes);
                this.weapon = null;
                this.health = 0;
            }

            public void attack(Damageable target) throws CharacterIsNotInPartyException {
                target.takeDamage(attributes.getStrength()+attributes.getIntelligence());
            }

            public void lootWeapon(Weapon weapon) {
            }

            public void levelUp() {
                level+=1;
                attributes.increaseIntelligence(1);
                attributes.increaseStrength(1);
            }
            public void takeHealing(int healing) {
                // Skeletons can't heal they are dead, you ever played minecraft?
                health -= healing;
            }
            public void takeDamage(int damage) {
                health -= damage;
            }
            public boolean isAlive() {
                return health > 0;
            }

            public String toString() {
                return "Skeleton LvL: " + this.getLevel() + " - " + this.getAttributes();
            }
        }


        class Warrior extends PlayableCharacter implements Combat {
            private Useable weapon;

            public Warrior(String name) {
                super(name, new Attributes(4, 2));
                this.health = 35;
            }

            public void takeHealing(int healing) {
                health +=healing;
                }

            public void levelUp() {
                level+=1;
                attributes.increaseIntelligence(1);
                attributes.increaseStrength(2);
            }

                public void attack(Damageable target) throws CharacterIsNotInPartyException {
                    target.takeDamage(attributes.getStrength()+weapon.use());
                }

            public void lootWeapon(Weapon weapon) {
                this.weapon = weapon;
                }

            public void takeDamage(int damage) {
                health -= damage;
            }

            public boolean isAlive() {
                return health > 0;
            }
        }


        class Cleric extends PlayableCharacter implements Caster {
            private Useable spell;

            public Cleric(String name) {
                super(name, new Attributes(2, 4));
                this.health = 25;
            }

            public void levelUp() {
                level+=1;
                attributes.increaseIntelligence(2);
                attributes.increaseStrength(1);
            }

            public void castSpell(Damageable target) {
                target.takeHealing(spell.use()+attributes.getIntelligence());
            }

            public String toString() {
                return "Cleric LvL: " + getLevel() + " - " + getAttributes();
            }

            public void takeDamage(int damage) {
                health -= damage;
                }

            public boolean isAlive() {
                return health > 0;
            }

            public void takeHealing(int healing) {
                health +=healing;
            }

            public void learnSpell(Spell spell) {
                this.spell=spell;
            }
        }


        class Paladin extends PlayableCharacter implements Combat, Caster {
            private Useable weapon;
            private Useable spell;

            public Paladin(String name) {
                super(name, new Attributes());
                this.health = 30;
            }

            public void levelUp() {
                if (getLevel() % 2 == 0) {
                    level+=1;
                    attributes.increaseIntelligence(1);
                    attributes.increaseStrength(2);
                } else {
                    level+=1;
                    attributes.increaseIntelligence(2);
                    attributes.increaseStrength(1);
                }
            }

            public void attack(Damageable target) throws CharacterIsNotInPartyException {
                target.takeDamage(attributes.getStrength()+weapon.use());
            }

            public void lootWeapon(Weapon weapon) {
                this.weapon = weapon;
            }

            public void castSpell(Damageable target) throws CharacterIsNotInPartyException {
                target.takeHealing(spell.use()+attributes.getIntelligence());
            }

            public void takeDamage(int damage) {
                    health -= damage;
                }


            public boolean isAlive() {
                return health > 0;
            }

            public void takeHealing(int healing) {
                health +=healing;
            }

            public boolean isInParty() {
                return super.isInParty();
            }

            public void joinParty(Party party) throws PartyLimitReachedException, AlreadyInPartyException {
                super.joinParty(party);
            }

            public void quitParty() throws CharacterIsNotInPartyException {
                super.quitParty();
            }

            public String toString() {
                return "Paladin LvL: " + getLevel() + " - " + getAttributes();
            }

            public void learnSpell(Spell spell) {
                this.spell=spell;
            }
        }


        class Party {

            private static final int partyLimit = 8;
            private List<PlayableCharacter> fighters = new ArrayList<>();
            private List<PlayableCharacter> healers = new ArrayList<>();
            private int mixedCount;

            public void addCharacter(PlayableCharacter character) throws PartyLimitReachedException,
                    AlreadyInPartyException {
                if (character.isInParty()) {
                    throw new AlreadyInPartyException("Can't join party");
                }
                if (fighters.size() + healers.size() - mixedCount >= partyLimit) {
                    throw new PartyLimitReachedException("Exceeds party limit");
                } else {
                    if (character instanceof Warrior) {
                        fighters.add(character);
                    } else if (character instanceof Cleric) {
                        healers.add(character);
                    } else if (character instanceof Paladin) {
                        fighters.add(character);
                        healers.add(character);
                        mixedCount++;
                    }
                }
            }

            public void removeCharacter(PlayableCharacter character) throws CharacterIsNotInPartyException {

                if (character instanceof Combat) {
                    fighters.remove(character);
                    if (character instanceof Paladin)
                        mixedCount--;
                }
                if (character instanceof Cleric) {
                    healers.remove(character);
                    if (character instanceof Paladin)
                        mixedCount--;
                }
                if (!character.isInParty())
                    throw new CharacterIsNotInPartyException("Character is not in party");
            }

            public void partyLevelUp() {
                for (PlayableCharacter character : fighters) {
                    if(character instanceof Paladin)
                        continue;
                    character.levelUp();
                }
                for (PlayableCharacter character : healers) {
                    character.levelUp();
                }
            }

            public String toString() {
                List <PlayableCharacter> sortedParty = new ArrayList<>();
                    sortedParty.addAll(fighters);

                    for (int i = 0; i < healers.size(); i++) {
                        if (!(healers.get(i) instanceof Paladin))
                            sortedParty.add(healers.get(i));
                    }
                    Collections.sort(sortedParty, (c1, c2) -> c1.getLevel() - c2.getLevel());
                    StringBuilder sb = new StringBuilder();
                    for (PlayableCharacter character : sortedParty) {
                        sb.append(character.toString()).append("\n");
                    }
                    return sb.toString();
                }
            }


        class Barrel implements Damageable {
        private int health = 30;
        private int capacity = 10;

        public void explode() {
            System.out.println("Explodes");
        }

        public void repair() {
            System.out.println("Repairing");
        }

        public void takeDamage(int damage) {
            health -= damage;
            if (health <= 0) {
                explode();
            }
        }

        public void takeHealing(int healing) {
            health+=healing;
            repair();
        }
            public boolean isAlive() {
                return health > 0;
            }
        }


        class TrainingDummy implements Damageable {
        private int health = 25;

        public void takeDamage(int damage) {
            health -= damage;
        }

        public void takeHealing(int healing) {
            health += healing;
        }
        public boolean isAlive() {
            return health > 0;
        }
        }


        class PartyLimitReachedException extends Exception {
    public PartyLimitReachedException(String String) {
        super("Party Limit Reached: " + String);
    }
}


         class AlreadyInPartyException extends Exception {
    public AlreadyInPartyException(String String) {
        super("Character Already In Party: " + String);
    }
}


         class CharacterIsNotInPartyException extends Exception {
    public CharacterIsNotInPartyException(String String) {
        super("Character Is Not In Party: " + String);
    }
}