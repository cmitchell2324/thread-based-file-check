package lvc.cds;

import java.io.*;
import java.util.*;

public class Check implements Runnable{
    private ArrayList<String> fileNames;
    private ArrayList<String> badPhrases;

    public Check(ArrayList<String> fileNames, ArrayList<String> badPhrases) {
        this.fileNames = fileNames;
        this.badPhrases = badPhrases;
    }

    public void run() {
        synchronized(this) {
            concurrentFileWork();
            notifyAll();
        }
    }

    public synchronized void concurrentFileWork() {
        ArrayList<Integer> counts = new ArrayList<>();
        ArrayList<String> fileSet = new ArrayList<>();
        for(String phrase : badPhrases) {
            int count = 0;
            int x = 0;
            for (String file : fileNames) {
                count = concurrentFileWork(file, phrase);
                if(count != 0) {
                    if(!fileSet.contains(file)) {
                        fileSet.add(file);
                    }
                }
                x += count;
            }
            counts.add(x);
        }

        for(String s : fileSet) {
            System.out.println(s + " needs moderated");
        }

        int[] x = new int[badPhrases.size()];
        for(int i = 0; i < counts.size(); i++) {
            x[i] = counts.get(i);
            System.out.println("Number of occurences of " + badPhrases.get(i) + ": " + x[i]);
        }
    }

    public int concurrentFileWork(String file, String phrase) {
        File files = new File(file);
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(files));
            String s;
            String[] words;
            while((s = reader.readLine()) != null) {
                words = s.split(" ");
                for(String word : words) {
                    if(word.toLowerCase().contains(phrase)) {
                        count++;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {}
        return count;
    }
}
