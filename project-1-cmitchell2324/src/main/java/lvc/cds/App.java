package lvc.cds;

import java.io.*;
import java.util.ArrayList;

public class App {

    /*
        This is the serial version of the code that contains no threads, in which I scanned each file individually. The program starts by utilizing the FileList.txt and BadPhrases.txt in order to create
        ArrayLists out of the items that are contained in the file. The FileList.txt contains the relative paths of the texts that I used to check my run time. This allows
        the code to be run on any computer. The BadPhrases.txt file is simply a list of a few "bad phrases" that are checked in each text file. Following this, I have a nested for loop
        that runs the method fileWorkSerial for each phrase in the BadPhrases.txt file. Each file is scanned individually for each word that is being checked. This serial version
        of the code tends to run much slower due to the fact that you are calling a method within a nested for loop. Also, the inner loop is running three times (for each of the
        designated files to check) for each word that is being checked. The count that is being returned from the fileWorkSerial method is being stored in the count variable and eventually
        added into an ArrayList of integers. These values will eventually be added into a separate counts array that will have the total of each bad phrase that spans across all files.
        Finally, I have a time check at the very end that determines how long it took to run the serial version of this code, from the instantiation of the first ArrayList to the very 
        end where the counts are put into an array. This version of the program is significantly slower than the thread-based concurrent version of the program and is complicated to follow.
    */

    private static BufferedReader reader = null;
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        // Instantiaion of ArrayList of filenames
        ArrayList<String> fileNames = new ArrayList<>();
        File fileList = new File("FileList.txt");
        reader = new BufferedReader(new FileReader(fileList));
        String line;
        while((line = reader.readLine()) != null) {
            fileNames.add(line);
        }

        // Instation of a File that contains all bad phrases
        File phrases = new File("BadPhrases.txt");
        ArrayList<String> badPhrases = new ArrayList<>();
        reader = new BufferedReader(new FileReader(phrases));
        String line1;
        while((line1 = reader.readLine()) != null) {
            badPhrases.add(line1);
        }

        // Counts ArrayList to keep track of all counts of each bad phrase
        ArrayList<Integer> counts = new ArrayList<>();

        // fileSet ArrayList that keeps track of files that need to be moderated
        ArrayList<String> fileSet = new ArrayList<>();
        for(String phrase : badPhrases) {
            int count = 0;
            int x = 0;
            for (String file : fileNames) {
                // Call to serial version of bad phrase checking
                count = fileWorkSerial(file, phrase);
                if(count != 0) {
                    if(!fileSet.contains(file)) {
                        fileSet.add(file);
                    }
                }
                x += count;
            }
            counts.add(x);
        }

        // If a file contains a bad phrase, it needs to be moderated
        for(String s : fileSet) {
            System.out.println(s + " needs moderated");
        }

        // For loop to print number of occurrences of each bad phrase totalled across all files
        int[] x = new int[badPhrases.size()];
        for(int i = 0; i < counts.size(); i++) {
            x[i] = counts.get(i);
            System.out.println("Number of occurences of " + badPhrases.get(i) + ": " + x[i]);
        }
        long end = System.currentTimeMillis() - start;
        System.out.println("The total time to run the non-synchronous version is: " + end + " ms");
    }

    public static int fileWorkSerial(String file, String phrase) throws IOException {
        // Creating a new File from the passed in argument
        File files = new File(file);
        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(files));
        String s;
        String[] words;
        while((s = reader.readLine()) != null) {
            words = s.split(" ");
            // Will count the word no matter if it is capital or lowercase
            for(String word : words) {
                if(word.toLowerCase().contains(phrase)) {
                    count++;
                }
            }
        }
        reader.close();
        // Count value to be pased back to main
        return count;
    }
}
