import util.Document;
import util.DocumentCollection;
import util.LogisticRegressionClassifier;
import util.NaiveBayesClassifier;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {
        Boolean executeNB = args[0].equals("nbc");
        Boolean removeStopWords = args[1].equals("true");

        if (removeStopWords)
            System.out.println("executing stop-words: REMOVED");
        else
            System.out.println("executing stop-words: NOT REMOVED");

        if (executeNB) {
            System.out.println("executing Naive Bayes Classifier");
            executeNB(removeStopWords);
        } else {
            System.out.println("executing Logistic Regression Classifier");
            executeLR(removeStopWords, args);
        }
    }

    private static void executeNB(Boolean removeStopWords) {

        //////////////
        // LEARNING //
        //////////////

        ArrayList<Document> trainingSpamDocuments = getDocuments("train/spam", removeStopWords);
        ArrayList<Document> trainingHamDocuments  = getDocuments("train/ham", removeStopWords);

        NaiveBayesClassifier nbc = new NaiveBayesClassifier(
                new DocumentCollection(trainingSpamDocuments),
                new DocumentCollection(trainingHamDocuments));

        /////////////
        // TESTING //
        /////////////

        ArrayList<Document> testSpamDocuments = getDocuments("test/spam", removeStopWords);
        ArrayList<Document> testHamDocuments = getDocuments("test/ham", removeStopWords);

        double correctCount = 0d;
        double totalDocuments = (double) (testSpamDocuments.size() + testHamDocuments.size());

        for (Document d: testSpamDocuments) {
            if (nbc.isDocumentSpam(d)) correctCount++;
        }
        for (Document d: testHamDocuments) {
            if (!nbc.isDocumentSpam(d)) correctCount++;
        }

        System.out.println("Naive Bayes Accuracy: " + correctCount/totalDocuments);
    }

    private static void executeLR(Boolean removeStopWords, String[] args) {
        Integer maxIterations = Integer.parseInt(args[2]); // 10;
        Double alpha = Double.parseDouble(args[3]); //0.01d;
        Double lambda = Double.parseDouble(args[4]); //0.0d;

        //////////////
        // LEARNING //
        //////////////

        ArrayList<Document> trainingSpamDocuments = getDocuments("train/spam", removeStopWords);
        ArrayList<Document> trainingHamDocuments  = getDocuments("train/ham", removeStopWords);

        LogisticRegressionClassifier lrc = new LogisticRegressionClassifier(
                trainingSpamDocuments,
                trainingHamDocuments,
                maxIterations,
                alpha,
                lambda);

        /////////////
        // TESTING //
        /////////////

        ArrayList<Document> testSpamDocuments = getDocuments("test/spam", removeStopWords);
        ArrayList<Document> testHamDocuments = getDocuments("test/ham", removeStopWords);

        double correctCount = 0d;
        double totalDocuments = (double) (testSpamDocuments.size() + testHamDocuments.size());

        for (Document d: testSpamDocuments) {
            if (lrc.isDocumentSpam(d)) correctCount++;
        }
        for (Document d: testHamDocuments) {
            if (!lrc.isDocumentSpam(d)) correctCount++;
        }

        System.out.println("Logistic Regression Accuracy: " + correctCount/totalDocuments);
    }

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
}
