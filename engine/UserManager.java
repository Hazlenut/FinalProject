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

    public static User getUser(String userName, String password) {

        User user = users.get(userName);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    public static User registerNewUser(String userName, String password) {

        if((userName.length() > 0) && (userName.length() <= WORD_LENGTH) && (password.length() > 0) && (password.length() < WORD_LENGTH) && !users.containsKey(userName)) {
            User user = new User(userName, password);
            users.put(userName, user);
            return user;
        }

        return null;
    }

    public static User removeUser(User user) {

        return users.remove(user.getUserName());
    }

    public static void save() throws IllegalStateException { //You must call this before exiting or user data will not be saved

        File[] files = userDirectory.listFiles();
        if(files != null) {
            for(File file : files) {
                file.delete();
            }
        }else if(!userDirectory.mkdir()){
            throw new IllegalStateException("Error saving to userdata");
        }
        int count = 0;
        while(!users.isEmpty()) {
            if(!Encryption.encryptAndWrite(new File(userDirectory.getPath() + "\\user" + (count++)), users.pollFirstEntry().getValue().getFileStrings())) {
                throw new IllegalStateException("Error while saving");
            }
        }

    }

    private static String removeExtraChars(String string) {

        return string.replaceAll(("" + (char)161), "").replaceAll(("" + (char)162), "").replaceAll(("" + (char)163), "")
                .replaceAll(("" + (char)164), "").replaceAll(("" + (char)165), "").replaceAll(("" + (char)166), "");
    }

    public static Collection<User> usersReadOnly() {

        return Collections.unmodifiableCollection(users.values());
    }

    public static boolean linkUsersToEvents() {

        if(!linked) {
            for(User user : users.values()) {
                for(EventManager.Event event : EventManager.getEvents()) {
                    if(user.getEventIDs().contains(event.getId())) {
                        user.getEventsAttending().add(event);
                    }
                }
            }
            linked = true;
            return true;
        }

        return false;
    }

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

        private User(String userName, String password) {

            this.userName = userName;
            this.password = password;
            eventsAttending = FXCollections.observableArrayList();

        }

        private void addEventIDS(ArrayList<Integer> links) {

            this.links = links;

        }

        public ArrayList<Integer> getEventIDs() {

            return links;
        }

        public String getUserName() {

            return userName;
        }

        public String getPassword() {

            return password;
        }

        public void setPassword(String password) {

            this.password = password;

        }

        public ObservableList<EventManager.Event> getEventsAttending() {

            return eventsAttending;
        }

        public boolean isAttending(EventManager.Event event) {

            for(EventManager.Event e : eventsAttending) {
                if(e.equals(event)) {
                    return true;
                }
            }

            return false;
        }

        private List<String> getFileStrings() {

            ArrayList<String> list = new ArrayList<>();
            list.add(userName);
            list.add(password);
            for(EventManager.Event event : eventsAttending) {
                list.add("" + event.getId());
            }
            for(int i = 0; i < list.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder(list.get(i));
                for(int j = 0; j < (WORD_LENGTH - password.length()); j++) {
                    stringBuilder.append(((char)(int)((Math.random() * 6) + 161)));
                }
                list.set(i, stringBuilder.toString());
            }

            return list;
        }

        @Override
        public String toString() {

            return userName;
        }

    }

}