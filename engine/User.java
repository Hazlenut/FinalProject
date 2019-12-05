package engine;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class User implements Comparable<User> {

    private static ArrayList<User> users;
    private static boolean initialized = false, encrypted;
    private static User currentUser;

    public static void initialize() {

        if(!initialized) {
            users = new ArrayList<>();
            getUserIDs();
            encrypted = Attributes.getAttribute("Encrypted").equalsIgnoreCase("Yes");
            currentUser = null;
            initialized = true;
        }

    }

    public static void setEncrypted(boolean encrypted) {

        if(encrypted) {
            Attributes.updateAttribute("Encrypted", "Yes", "No");
        }else{
            Attributes.updateAttribute("Encrypted", "No", "Yes");
        }
        User.encrypted = encrypted;

    }

    private static String encrypt(String text, boolean decrypt) {

        int offset = decrypt ? -4 : 4;

        StringBuilder s = new StringBuilder();
        for(char c : text.toCharArray()) {
            s.append((char)((int)c + offset));
        }

        return s.toString();
    }

    private static void getUserIDs() {

        boolean encrypted = Attributes.getAttribute("Encrypted").equals("Yes");
        try(Scanner input = new Scanner(new File("userIDs.txt"))) {
            while(input.hasNext()) {
                if(encrypted) {
                    users.add(new User(encrypt(input.nextLine(), true)));
                }else{
                    users.add(new User(input.nextLine()));
                }
            }
        }catch(FileNotFoundException e) {
            //users.size() stays at 0
        }

    }

    public static boolean save() {

        if(userFileExists()) {
            try(FileWriter fileWriter = new FileWriter(new File(Main.getDirectory() + "userIDs.txt"))) {
                Iterator<User> users = User.users.iterator();
                while(users.hasNext()) {
                    if(encrypted) {
                        fileWriter.write(encrypt(users.next().userID, false));
                    }else{
                        fileWriter.write(users.next().userID);
                    }
                    if(users.hasNext()) {
                        fileWriter.write("\n");
                    }
                }
            }catch(IOException e2) {
                return false;
            }
        }

        return true;
    }

    public static void logOutCurrentUser() {

        save();
        if(currentUser != null) {
            EventLibrary.save(User.getCurrentUser().getUserID());
            EventLibrary.clear();
        }
        currentUser = null;

    }

    public static User getCurrentUser() {

        return currentUser;
    }

    private static void addUser(User user) {

        users.add(user);
        currentUser = user;

    }

    public static boolean logUserIn(String userID) {

        User user = getUser(userID);
        if(currentUser == null && user != null) {
            currentUser = user;
            return true;
        }

        return false;
    }

    public static void registerNewUser() {

        if(currentUser == null) {
            addUser(new User(generateID()));
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Your userID is " + User.getCurrentUser().getUserID());
            a.setTitle("Event Organizer");
            a.setHeaderText("Welcome to Event Organizer!");
            a.show();
        }

    }

    public static boolean userFileExists() {

        return users.size() > 0;
    }

    public static boolean validateUserID(String userID) {

        for(User user : users) {
            if(user.userID.equals(userID)) {
                return true;
            }
        }

        return false;
    }

    private static String generateID() {

        String id;
        do{
            id = "user" + (short)(Math.random() * Short.MAX_VALUE);
        }while(validateUserID(id));

        return id;
    }

    public static User getUser(String userID) {

        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }

        return null;
    }

    public static ArrayList<User> getUserList() {

        return new ArrayList<>(User.users);
    }

    private String userID;

    private int factor;
    private int points;

    private User(String userID) {

        this.userID = userID;
        factor = 5;

    }

    public String getUserID() {

        return userID;
    }

    public void setUserID(String userID) {

        this.userID = userID;

    }

    public int getFactor() {

        return factor;
    }

    public void setFactor(int factor) {

        this.factor = factor;

    }

    public int getPoints() {

        return points;
    }

    public void setPoints(int points) {

        this.points = points;

    }

    public void addPoints() {

        this.points++;

    }

    public void removePoints() {

        this.points--;

    }

    @Override
    public int compareTo(User user) {

        return Integer.compare(points, user.points);
    }

}