package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public final class Attributes {
    
    private static String directory;
    
    private static HashMap<String, String> attributes;
    private static PriorityQueue<String> defaultValues;
    private static boolean stateChanged;

    private static final String DEFAULT_ATTRIBUTE = "null"; //To prevent NullPointerExceptions

    static {
        
        directory = Main.getDirectory();
        stateChanged = false;
        attributes = new HashMap<>();
        defaultValues = new PriorityQueue<>();
        try(Scanner input = new Scanner(new File(directory + "attributes.txt"))) {
            while(input.hasNext()) {
                String next = input.nextLine();
                attributes.put(next.substring(0, next.indexOf('=')), next.substring(next.indexOf('=') + 1, next.length()));
            }
        }catch(FileNotFoundException | StringIndexOutOfBoundsException e) {
            initializeDefaultAttributes();
        }

    }

    private static void initializeDefaultAttributes() {

        defaultValues.offer("Title=Event Organizer");
        defaultValues.offer("Previously Loaded=No");

        try(FileWriter writer = new FileWriter(new File(directory + "attributes.txt"))) {
            while(!defaultValues.isEmpty()) {
                String string = defaultValues.remove();
                writer.write(string);
                if(defaultValues.size() > 0) {
                    writer.write("\n");
                }
                attributes.put(string.substring(0, string.indexOf('=')), string.substring(string.indexOf('=') + 1, string.length()));
            }
        }catch(IOException e2) {
            while(!defaultValues.isEmpty()) {
                String string = defaultValues.remove();
                attributes.put(string.substring(0, string.indexOf('=')), string.substring(string.indexOf('=') + 1, string.length()));
            }
        }

    }

    public static void save() {

        if(stateChanged) {
            for(String key : attributes.keySet()) {
                defaultValues.add(key + "=" + attributes.get(key));
            }
            try(FileWriter fileWriter = new FileWriter(new File(directory + "attributes.txt"))) {
                while(!defaultValues.isEmpty()) {
                    fileWriter.write(defaultValues.remove());
                    if(!defaultValues.isEmpty()) {
                        fileWriter.write('\n');
                    }
                }
            }catch(IOException e) {}
        }

    }

    public static String getAttribute(String type) {

        String attribute = attributes.get(type);
        if(attribute == null) {
            return DEFAULT_ATTRIBUTE;
        }else{
            return attribute;
        }

    }

    public static boolean updateAttribute(String type, String oldAttribute, String newAttribute) {

        if(attributes.containsKey(type)) {
            stateChanged = attributes.replace(type, oldAttribute, newAttribute);
        }

        return stateChanged;
    }

}