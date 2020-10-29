package lvc.cds;
import java.util.*;
import java.io.*;

public class Test implements Runnable {
    private ArrayList<String> badPhrases;
    private int[] count = new int[6];
    private File fileWork;
    private ArrayList<String> fileSet;
    private String file;

    public Test(String file, int[] count) {
        this.count = count;
        this. file = file;
        this.fileWork = new File(file);
        File phrases = new File("BadPhrases.txt");
        this.badPhrases = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new FileReader(phrases))) {
            String line1;
            while((line1 = reader.readLine()) != null) {
                this.badPhrases.add(line1);
            }
        } catch (IOException e) {}
        this.fileSet = new ArrayList<String>();
    }

    public void run() {
        concurrentWork(this.count); 
        for(int i = 0; i < this.count.length; i++) {
            System.out.println("Phrase: " + this.badPhrases.get(i) + "||" + this.count[i]);
            if(this.count[i] != 0) {
                if(!fileSet.contains(this.file)) {
                    fileSet.add(this.file);
                }
            }
        }
        
        for(String s : fileSet) {
            System.out.println(s + " needs moderated");
        }
    }
    
    public synchronized void concurrentWork(int[] count1) {
        ArrayList<Integer> counter = new ArrayList<>();

        for(String phrase : badPhrases) {
            int counts = 0;
            try(BufferedReader reader = new BufferedReader(new FileReader(this.fileWork))) {
                String s;
                String[] words;
                while((s = reader.readLine()) != null) {
                    words = s.split(" ");
                    for(String word : words) {
                        if(word.toLowerCase().contains(phrase)) {
                            counts++;
                        }
                    }
                }
                counter.add(counts);
            } catch (IOException e) {}
        }

        for(int i = 0; i < count1.length; i++) {
            count1[i] += counter.get(i);
        }
    }
}
