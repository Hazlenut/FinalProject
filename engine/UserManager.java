package engine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.*;

//Can't be extended
public final class UserManager {

    //Can't be instantiated
    private UserManager() {}

    //Constants:
    private static final int WORD_LENGTH = 32; //Maximum character size of username & password

    //Static Variables:
    private static boolean initialized = false, linked = false;
    private static File userDirectory; //Directory to store user data
    private static TreeMap<String, User> users;

    /**
     * This method initializes the User system and loads in previously saved users
     * @param path the path for the userDirectory
     * @throws IllegalStateException if this event is called more than once
     */
    public static void initialize(String path) throws IllegalStateException { //Only call once every time the application is run, at the very start of the program

        if(!initialized) {
            userDirectory = new File(path + "userdata");
            users = new TreeMap<>();
            if(!UserManager.userDirectory.mkdir()) {
                try {
                    for(File file : UserManager.userDirectory.listFiles()) {
                        Scanner input = new Scanner(Encryption.readAndDecrypt(file));
                        User user = new User(removeExtraChars(input.nextLine()), removeExtraChars(input.nextLine()));
                        ArrayList<Integer> links = new ArrayList<>();
                        while(input.hasNextLine()) {
                            links.add(Integer.parseInt(removeExtraChars(input.nextLine())));
                        }
                        user.addEventIDS(links);
                        users.put(user.getUserName(), user);
                        input.close();
                    }
                }catch(NoSuchElementException | NullPointerException e) {
                    throw new IllegalStateException("Corrupted user file detected");
                }
            }
            initialized = true;
        }else{
            throw new IllegalStateException("User manager has already been initialized");
        }

    }

    /**
     * @return the User object , or null if no user exists with the specified username & password
     */
    public static User getUser(String userName, String password) {

        User user = users.get(userName);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    /**
     * This method creates a new User
     * @param userName the username of the new user
     * @param password the password of the new user
     * @return the new User object (or null if the User couldn't be created)
     */
    public static User registerNewUser(String userName, String password) {

        if((userName.length() > 0) && (userName.length() <= WORD_LENGTH) && (password.length() > 0) && (password.length() < WORD_LENGTH) && !users.containsKey(userName)) {
            User user = new User(userName, password);
            users.put(userName, user);
            return user;
        }

        return null;
    }

    /**
     * Deletes the specified user from the program
     * @param user the User to be deleted
     */
    public static void removeUser(User user) {

        users.remove(user.getUserName());
    }

    /**
     * This method must be called before exiting or else userdata won't be saved
     */
    public static void save() throws IllegalStateException { //You must call this before exiting or user data will not be saved

        //Delete old user files, as they are now outdated:
        File[] files = userDirectory.listFiles();
        if(files != null) {
            for(File file : files) {
                file.delete();
            }
        }else if(!userDirectory.mkdir()){
            throw new IllegalStateException("Error saving to userdata"); //throw an exception if the userdata directory could not be created
        }
        int count = 0;
        while(!users.isEmpty()) {
            if(!Encryption.encryptAndWrite(new File(userDirectory.getPath() + "\\user" + (count++)), users.pollFirstEntry().getValue().getFileStrings())) {
                throw new IllegalStateException("Error while saving"); //throw an exception if an error occurs while saving
            }
        }

    }

    /**
     * This method removes unnecessary characters from a String, that were added for encryption purposes while saving to a file
     * @param string the String with extra characters
     * @return the String with the extra characters removed
     */
    private static String removeExtraChars(String string) {

        return string.replaceAll(("" + (char)161), "").replaceAll(("" + (char)162), "").replaceAll(("" + (char)163), "")
                .replaceAll(("" + (char)164), "").replaceAll(("" + (char)165), "").replaceAll(("" + (char)166), "");
    }

    /**
     * @return an unmodifiable List of all Users
     */
    public static Collection<User> usersReadOnly() {

        return Collections.unmodifiableCollection(users.values());
    }

    /**
     * This method adds every event that every user has an id for to that same user's eventsAttending ArrayList
     */
    public static void linkUsersToEvents() {

        if(!linked) {
            for(User user : users.values()) {
                for(EventManager.Event event : EventManager.getEvents()) {
                    if(user.getEventIDs().contains(event.getId())) {
                        user.getEventsAttending().add(event);
                    }
                }
            }
            linked = true;
        }

    }

    /**
     * This method checks how many users are attending a given event
     * @param event the Event being checked
     * @return the number of users attending the event
     */
    public static int usersAttendingEvent(EventManager.Event event) {

        int count = 0;
        for(User user : users.values()) {
            if(user.getEventsAttending().contains(event)) {
                count++;
            }
        }

        return count;
    }

    public static class User {

        private String userName, password;
        private ObservableList<EventManager.Event> eventsAttending;
        private ArrayList<Integer> links;

        /**
         * Creates a new User with the specified username and password
         */
        private User(String userName, String password) {

            this.userName = userName;
            this.password = password;
            eventsAttending = FXCollections.observableArrayList();

        }

        /**
         * This method adds the ArrayList of Event IDs to this User (for loading purposes)
         * @param links
         */
        private void addEventIDS(ArrayList<Integer> links) {

            this.links = links;

        }

        /**
         * @return the ArrayList of Event IDS that this user has
         */
        public ArrayList<Integer> getEventIDs() {

            return links;
        }

        /**
         * @return this Users username as a String
         */
        public String getUserName() {

            return userName;
        }

        /**
         * @return this Users password as a String
         */
        public String getPassword() {

            return password;
        }

        /**
         * @return an ObservableList of all Events this User is attending
         */
        public ObservableList<EventManager.Event> getEventsAttending() {

            return eventsAttending;
        }

        /**
         * @param event the Event to check attendance
         * @return true if the user is attending; false otherwise
         */
        public boolean isAttending(EventManager.Event event) {

            for(EventManager.Event e : eventsAttending) {
                if(e.equals(event)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * @return a List of Strings representing this User (for writing to a file)
         */
        private List<String> getFileStrings() {

            //Create an ArrayList to store the fields:
            ArrayList<String> list = new ArrayList<>();
            list.add(userName);
            list.add(password);
            for(EventManager.Event event : eventsAttending) {
                list.add("" + event.getId());
            }
            for(int i = 0; i < list.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder(list.get(i));
                for(int j = 0; j < (WORD_LENGTH - password.length()); j++) {
                    stringBuilder.append(((char)(int)((Math.random() * 6) + 161))); //Adds in random filler characters so that all lines in the file are the same length
                }
                list.set(i, stringBuilder.toString());
            }

            return list;
        }

        /**
         * @return a String representation of this User
         */
        @Override
        public String toString() {

            return userName;
        }

    }

}