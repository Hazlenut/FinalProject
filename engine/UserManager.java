package engine;

import java.io.File;
import java.util.*;

//Can't be extended
public final class UserManager {

    //Can't be instantiated
    private UserManager() {}

    //Constants:
    private static final int WORD_LENGTH = 32; //Maximum character size of username & password

    //Static Variables:
    private static boolean initialized = false;
    private static File directory; //Directory to store user data
    private static TreeMap<String, User> users;

    public static void initialize(String path) throws IllegalStateException { //Only call once every time the application is run, at the very start of the program

        if(!initialized) {
            directory = new File(path + "userdata");
            users = new TreeMap<>();
            if(!UserManager.directory.mkdir()) {
                try {
                    for(File file : UserManager.directory.listFiles()) {
                        Scanner input = new Scanner(Encryption.readAndDecrypt(file));
                        User user = new User(removeExtraChars(input.nextLine()), removeExtraChars(input.nextLine()));
                        //set other user data with remaining scanner fields
                        users.put(user.getUserName(), user);
                        input.close();
                    }
                }catch(NoSuchElementException e) {
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

    public static int numActiveUsers() {

        return users.size();
    }

    public static void save() throws IllegalStateException { //You must call this before exiting or user data will not be saved

        File[] files = directory.listFiles();
        if(files != null) {
            for(File file : files) {
                file.delete();
            }
        }else if(!directory.mkdir()){
            throw new IllegalStateException("Error saving to userdata");
        }
        int count = 0;
        while(!users.isEmpty()) {
            if(!Encryption.encryptAndWrite(new File(directory.getPath() + "\\user" + (count++)), users.pollFirstEntry().getValue().getFileLines())) {
                throw new IllegalStateException("Error while saving");
            }
        }

    }

    private static String removeExtraChars(String string) {

        return string.replaceAll(("" + (char)161), "").replaceAll(("" + (char)162), "").replaceAll(("" + (char)163), "")
                .replaceAll(("" + (char)164), "").replaceAll(("" + (char)165), "").replaceAll(("" + (char)166), "");
    }

    public static class User {

        private String userName, password;

        private User(String userName, String password) {

            this.userName = userName;
            this.password = password;

        }

        public String getUserName() {

            return userName;
        }

        public void setUserName(String userName) {

            this.userName = userName;

        }

        public String getPassword() {

            return password;
        }

        public void setPassword(String password) {

            this.password = password;

        }

        //Data field operations...

        private List<String> getFileLines() { //Add data fields here too

            ArrayList<String> list = new ArrayList<>();
            //Add in the order that you want them printed; no field should exceed WORD_LENGTH
            list.add(userName);
            list.add(password);
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
        public String toString() { //Change to reflect relevant data fields

            return userName;
        }

    }

}