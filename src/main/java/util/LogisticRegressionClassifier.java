package util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LogisticRegressionClassifier {

    // Y - binary output {spam, not-spam}
    // X - input vector where each Xi contains count of each term in vocabulary
    // W - weight vector

    Map<String, Double> weightVector; // <term, weight>

    public LogisticRegressionClassifier(ArrayList<Document> spamDocuments, ArrayList<Document> hamDocuments, Integer maxIterations, Double alpha, Double lambda) {
        this.weightVector = new TreeMap<>();

        Set<String> vocabulary = DocumentCollection.getVocabulary(new DocumentCollection(spamDocuments), new DocumentCollection(hamDocuments));
        for (String term : vocabulary) {
            this.weightVector.put(term, .001d); //(double)(Math.random() <= 0.5 ? 1 : -1)
        }

        this.gradientDescent(spamDocuments, hamDocuments, maxIterations, alpha, lambda);
    }

    /**
     * gradient descent with L2 regularization
     * Wj := Wj - (α/m) * [ (∑(h(Xⁱ) - yⁱ)Xjⁱ) + λWj ]
     * @param spamDocuments
     * @param hamDocuments
     * @param maxIterations
     * @param lambda
     */
    private void gradientDescent(ArrayList<Document> spamDocuments, ArrayList<Document> hamDocuments, Integer maxIterations, Double alpha,  Double lambda) {
        Double alphaOverM = alpha / (double)(spamDocuments.size() + hamDocuments.size());

        Map<String, Double> deltaWeightVector = new TreeMap<>();
        for (int i = 0; i < maxIterations; i++) {
            for (Map.Entry<String, Double> entry : this.weightVector.entrySet()) {
                String term = entry.getKey();
                Double termWeight = entry.getValue();
                deltaWeightVector.put(
                        term,
                        deltaWeight(term, termWeight, spamDocuments, hamDocuments, alphaOverM, lambda));
            }

            for (Map.Entry<String, Double> entry : this.weightVector.entrySet()) {
                String term = entry.getKey();
                this.weightVector.put(
                        term,
                        entry.getValue() - deltaWeightVector.get(term));
            }
            System.out.println("iteration " + (i+1) + " finished");
        }
    }

    /**
     *
     * @param term
     * @param termWeight
     * @param spamDocuments
     * @param hamDocuments
     * @param alphaOverM - (α/m)
     * @param lambda
     * @return (α/m) * [ (∑(h(Xⁱ) - yⁱ)Xjⁱ) + λWj ]
     */
    private Double deltaWeight(String term, Double termWeight, ArrayList<Document> spamDocuments, ArrayList<Document> hamDocuments, Double alphaOverM,  Double lambda) {
        Double deltaWeight = 0d;

        for (Document d : spamDocuments) {
            if (!isDocumentSpam(d)) {
                deltaWeight -= (double)d.getTokenFrequency().getOrDefault(term, 0);
            }
        }

        for (Document d : hamDocuments) {
            if (isDocumentSpam(d)) {
                deltaWeight += (double)d.getTokenFrequency().getOrDefault(term, 0);
            }
        }

        deltaWeight += (lambda * termWeight);
        deltaWeight *= alphaOverM;
        return deltaWeight;
    }

    public Boolean isDocumentSpam(Document document) {
        Double value = 0d;
        for (Map.Entry<String, Integer> entry : document.getTokenFrequency().entrySet()) {
            value += this.weightVector.getOrDefault(entry.getKey(), 0d) * entry.getValue();
        }

        if (value > 0) return true;
        if (value < 0) return false;
//        if (value.equals(0d)) System.out.println("value equals 0.0: assign as spam");
        return true;
    }

}
