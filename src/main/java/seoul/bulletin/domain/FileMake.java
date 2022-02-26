package seoul.bulletin.domain;

import java.io.*;

public class FileMake {

    public static void main(String[] args) {
        try (
                FileWriter fileWriter = new FileWriter("test.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write("mtak");
            bufferedWriter.newLine();
            bufferedWriter.write("yelo");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File("test.txt");
        if (f.isFile()) {
            System.out.println("test.txt file exists");
        }

        try (
                FileReader fileReader = new FileReader("test.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            String readline = null;
            while ((readline = bufferedReader.readLine()) != null) {
                System.out.println(readline);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
