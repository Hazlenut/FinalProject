package engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//Can't be extended
public final class Encryption {

    //Can't be instantiated
    private Encryption() {}

    private static final int ENCRYPTION_OFFSET = 5; //characters will be shifted down and up by this number when read and written respectively to encrypt data

    public static boolean encryptAndWrite(File file, List<String> data) {

        try(FileWriter fileWriter = new FileWriter(file)) {
            for(int i = 0; i < data.size(); i++) {
                for(char c : data.get(i).toCharArray()) {
                    fileWriter.write((char)(((int)c) + ENCRYPTION_OFFSET));
                }
                if(i != (data.size() - 1)) {
                    fileWriter.write("\n");
                }
            }
        }catch(IOException e) {
            return false;
        }

        return true;
    }

    public static String readAndDecrypt(File file) {

        try(Scanner input = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while(input.hasNextLine()) {
                for(char c : input.nextLine().toCharArray()) {
                    builder.append((char)(((int)c) - ENCRYPTION_OFFSET));
                }
                builder.append('\n');
            }
            return builder.toString();
        }catch(IOException e) {
            return null;
        }

    }

}