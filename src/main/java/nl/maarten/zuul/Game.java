package nl.maarten.zuul;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Room> rooms;
    private ArrayList<Item> items;
    private static Player player;
        
    /**
     * Create the game and initialise its internal map.
     */
    public static void main(String[] args) {
    	System.out.print("Hello, what's your name? ");
    	Scanner scanner = new Scanner(System.in);
    	player = new Player(scanner.nextLine());
    	
        new Game().play();
    }

    public Game() 
    {
    	createItems();
        createRooms();
        parser = new Parser();
    }

    private void createItems() {
    	Item key, pencil, notebook, bottle, glass, dollarbill;
    	items = new ArrayList<>();
    	
    	key = new Item("key", "with a key you can open doors", 20);
    	items.add(key);
    	pencil = new Item("pencil", "with a pencil you can write", 20);
    	items.add(pencil);
    	notebook = new Item("notebook", "with a notebook you can remember", 20);
    	items.add(notebook);
    	bottle = new Item("bottle", "with a bottle you can pour drinks", 20);
    	items.add(bottle);
    	glass = new Item("glass", "with a glass you can drink beer", 20);
    	items.add(glass);
    	dollarbill = new Item("dollarbill", "with a dollarbill you can buy yourself a drink", 20);
    	items.add(dollarbill);
    	
    }
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, transporter;
        rooms = new ArrayList<>();
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        rooms.add(outside);
        
        for (Item item : items) {
        	outside.addItem(item);
        }
        theater = new Room("in a lecture theater");
        rooms.add(theater);
        pub = new Room("in the campus pub");
        rooms.add(pub);
        lab = new Room("in a computing lab");
        rooms.add(lab);
        office = new Room("in the computing admin office");
        rooms.add(office);
        
        transporter = new TransporterRoom("in a transporter room", rooms);
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", transporter);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        transporter.setExit("anywhere", transporter);

        currentRoom = outside;  // start game outside
        player.setCurrentRoom(currentRoom);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {      
    	printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul, " + player.getName() + "!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case TAKE:
            	takeItem(command);
                break;

            case DROP:
                dropItem(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            player.setCurrentRoom(currentRoom);
            System.out.println(currentRoom.getLongDescription());
            System.out.println(player.getItemsString());
        }
    }

    private void takeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Take what?");
            return;
        }

        String name = command.getSecondWord();

        Item item = currentRoom.getItem(name);

        if (item == null) {
            System.out.println("There is no item!");
        }
        else {
            currentRoom.removeItem(item);
            player.takeItem(item);
            System.out.println(currentRoom.getLongDescription());
            System.out.println(player.getItemsString());
        }
    }

    private void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Drop what?");
            return;
        }

        String name = command.getSecondWord();

        Item item = null;
        for (Item it : items) {
            if (it.getName().equals(name)) {
                item = it;
                break;
            }
        }

        if (item == null) {
            System.out.println("There is no item!");
        }
        else {
            currentRoom.addItem(item);
            player.dropItem(item);
            System.out.println(currentRoom.getLongDescription());
            System.out.println(player.getItemsString());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
