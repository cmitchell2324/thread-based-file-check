package lvc.cds;
import java.io.*;
import java.util.*;


/*
    This is the synchronous version of the code. My plan of attack for this was to create a thread for each file that there was and check for each of the bad phrases. At the very end
    a count is printed out which illustrates each word and its total count across all of the files. As the check is going on, each file is being checked for moderation if it contains 
    any bad phrases. All methods are appropriately synchronized so each thread will acquire the lock and release once it leaves the scope of the concurrentWork method. This version
    is slower than the thread-based serial version because you are creating three different threads to check three different files. It would be faster to check three files on one
    thread since they are all doing the
*/



public class SynchronousCheck {
    private static BufferedReader reader = null;
    private static int[] count = new int[6];
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        ArrayList<String> fileNames = new ArrayList<>();
        File fileList = new File("FileList.txt");
        reader = new BufferedReader(new FileReader(fileList));
        String line;
        while((line = reader.readLine()) != null) {
            fileNames.add(line);
        }

        File phrases = new File("BadPhrases.txt");
        ArrayList<String> badPhrases = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new FileReader(phrases))) {
            String line1;
            while((line1 = reader.readLine()) != null) {
                badPhrases.add(line1);
            }
        } catch (IOException e) {}

        for(String file : fileNames) {
            Thread thread = new Thread(new Test(file, count));
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
        printCount(count, badPhrases);
        long end = System.currentTimeMillis() - start;
        System.out.println("Total time: " + end + " ms");
    }

    public static void printCount(int[] count1, ArrayList<String> badPhrases) {
        for(int i = 0; i < count.length; i++) {
            System.out.println("Final Count of: " + badPhrases.get(i) + " is: " + count[i]);
        }
    }
}

