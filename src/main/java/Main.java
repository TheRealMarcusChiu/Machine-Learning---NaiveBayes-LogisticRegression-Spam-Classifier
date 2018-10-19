import util.Document;
import util.DocumentCollection;
import util.LogisticRegressionClassifier;
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

    private static ArrayList<Document> getDocuments(String directoryPath, Boolean removeStopWords) {
        File[] files = new File(directoryPath).listFiles();
        ArrayList<Document> documents = new ArrayList<>();
        for (File f : files) {
            try {
                documents.add(new Document(fileToString(f), removeStopWords));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documents;
    }

    public static void main(String args[]) {
        Boolean removeStopWords = false;

        //////////////
        // LEARNING //
        //////////////

        DocumentCollection spamDocumentCollection = new DocumentCollection(getDocuments("train/spam", removeStopWords));
        DocumentCollection hamDocumentCollection  = new DocumentCollection(getDocuments("train/ham", removeStopWords));

        NaiveBayesClassifier nbc = new NaiveBayesClassifier(spamDocumentCollection, hamDocumentCollection);
        LogisticRegressionClassifier lrc = new LogisticRegressionClassifier(spamDocumentCollection, hamDocumentCollection);

        /////////////
        // TESTING //
        /////////////

        ArrayList<Document> testSpamDocuments = getDocuments("test/spam", removeStopWords);
        ArrayList<Document> testHamDocuments = getDocuments("test/ham", removeStopWords);

        double nbCorrectCount = 0d;
        double lrCorrectCount = 0d;
        double totalDocuments = (double) (testSpamDocuments.size() + testHamDocuments.size());

        for (Document d: testSpamDocuments) {
            if (nbc.isDocumentSpam(d)) nbCorrectCount++;
            if (lrc.isDocumentSpam(d)) lrCorrectCount++;
        }
        for (Document d: testHamDocuments) {
            if (!nbc.isDocumentSpam(d)) nbCorrectCount++;
            if (!lrc.isDocumentSpam(d)) lrCorrectCount++;
        }

        System.out.println("Naive Bayes Accuracy: " + nbCorrectCount/totalDocuments);
        System.out.println("Logistic Regression Accuracy: " + lrCorrectCount/totalDocuments);
    }
}
