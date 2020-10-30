package lvc.cds;

import java.util.*;
import java.io.*;

public class ConcurrentFileCheck {
    
    /*
        This is the concurrent version of the program that I came up with. While the working code is essentially the same as the serial version, it utilizes a thread, which essentially
        speeds up the runtime by a significant amount. I added an artificial wait time after the thread starts in order to have the total time print out at the very end. In this
        version of the code, the program once again starts off by creating an ArrayList of both the bad phrases and the files to be checked. Then, I created a new Thread that will
        execute the run() method from the Check class. This occurs because Check implements Runnable, so the Thread knows to access this run() method. I initially started out by creating
        a thread for each time a work is checked, but this slowed down the run time by a significant amount. In fact, it ended up being slower than the serial verison of the code.
        This idea was very similar to what we discussed in class, where we were creating multiple threads to check prime numbers. It ended up being slower than the non-synchronous
        version because instead of having one thread check multiple primes, we had multiple threads checking multiple primes, which ultimately slows things down. When looking at the
        Check class, I made the concurrentFileWork method synchronized so if, by chance, another thread was decided to be created, data will be protected. The thread will acquire the
        lock of the concurrentFileWork method and release it once the method scope is done. I did not need to synchronize the int concurrentFileWork method because it is only being
        accessed within the synchronized method, so there would be no thread interference. Just as we discussed in class, it is easier to check a bunch of phrases with one thread instead
        of checking individual phrases on individual threads. This version is faster than the thread version in SynchronousCheck because it is creating one thread that does all of the
        work instead of creating three threads for each file.   
    */

    private static BufferedReader reader = null;
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        // Instantiation of fileNames ArrayList that will contain the name of each text file to be checked
        ArrayList<String> fileNames = new ArrayList<>();
        File fileList = new File("FileList.txt");
        reader = new BufferedReader(new FileReader(fileList));
        String line;
        while((line = reader.readLine()) != null) {
            fileNames.add(line);
        }

        File phrases = new File("BadPhrases.txt");
        ArrayList<String> badPhrases = new ArrayList<>();
        reader = new BufferedReader(new FileReader(phrases));
        String line1;
        while((line1 = reader.readLine()) != null) {
            badPhrases.add(line1);
        }
    
        // Launches a thread that will check all files in fileNames against all bad phrases in badPhrases.
        Thread thread = new Thread(new Check(fileNames, badPhrases));
        thread.start();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {}
        long end = System.currentTimeMillis() - start;
        System.out.println("The total time to do the synchronous version is: " + end + " ms");
    }
}
