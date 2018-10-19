import util.Document;
import util.DocumentCollection;
import util.NaiveBayesClassifier;

import java.io.*;
import java.util.ArrayList;

public class Main {

    private static String fileToString(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    private static ArrayList<Document> getDocuments(String directoryPath) {
        File[] files = new File(directoryPath).listFiles();
        ArrayList<Document> documents = new ArrayList<>();
        for (File f : files) {
            try {
                documents.add(new Document(fileToString(f), true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documents;
    }

    public static void main(String args[]) {
        DocumentCollection spamDocumentCollection = new DocumentCollection(getDocuments("train/spam"));
        DocumentCollection hamDocumentCollection  = new DocumentCollection(getDocuments("train/ham"));

        NaiveBayesClassifier nbc = new NaiveBayesClassifier(spamDocumentCollection, hamDocumentCollection);


        ArrayList<Document> testSpamDocuments = getDocuments("test/spam");
        double falseNegativeCount = 0d;
        for (Document d: testSpamDocuments) {
            if (!nbc.isDocumentSpam(d)) falseNegativeCount++;
        }
        System.out.println("false negative count: " + falseNegativeCount);
        System.out.println("num test spam documents: " + testSpamDocuments.size());


        ArrayList<Document> testHamDocuments = getDocuments("test/ham");
        double falsePositiveCount = 0d;
        for (Document d: testHamDocuments) {
            if (nbc.isDocumentSpam(d)) falsePositiveCount++;
        }
        System.out.println("false positive count: " + falsePositiveCount);
        System.out.println("num test ham documents: " + testHamDocuments.size());
    }
}
