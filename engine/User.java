package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class User implements Comparable<User> {

    private static PriorityQueue<User> users;

    public static void initialize() {

        users = new PriorityQueue<>();
        getUserIDs();

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
            //userIDs queue stays at size 0
        }

    }

    public static boolean save(boolean encrypted) {

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

    public static void addUser(String userID) {

        users.add(new User(userID));

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

    public static String generateID() {

        String id;
        do{
            id = "user" + (short)(Math.random() * Short.MAX_VALUE);
        }while(validateUserID(id));

        return id;
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