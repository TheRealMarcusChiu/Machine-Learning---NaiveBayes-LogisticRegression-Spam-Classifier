package util;

import lombok.Data;

import java.util.*;

@Data
public class NaiveBayesClassifier {

    Double logProbabilitySpam; // log[P(spam)]
    Double logProbabilityHam;  // log[P(!spam)]
    Map<String, Double> conditionalLogProbabilitiesSpam; // log[P(t|spam)]
    Map<String, Double> conditionalLogProbabilitiesHam;  // log[P(t|!spam)]

    Set<String> vocabulary;

    public NaiveBayesClassifier(DocumentCollection spamDocuments, DocumentCollection hamDocuments) {
        int n = spamDocuments.getNumDocuments() + hamDocuments.getNumDocuments();
        this.logProbabilitySpam = Math.log((double)spamDocuments.getNumDocuments() / (double)n);
        this.logProbabilityHam  = Math.log((double)hamDocuments.getNumDocuments()  / (double)n);
        this.conditionalLogProbabilitiesSpam = new TreeMap<>();
        this.conditionalLogProbabilitiesHam = new TreeMap<>();

        this.vocabulary = this.getVocabulary(spamDocuments, hamDocuments);

        int laplaceDenominatorSpam = spamDocuments.getTotalTokens() + vocabulary.size();
        int laplaceDenominatorHam  =  hamDocuments.getTotalTokens() + vocabulary.size();

        for(String term : vocabulary) {
            int spamValue = spamDocuments.getTokenFrequency().getOrDefault(term, 0) + 1;
            int hamValue  =  hamDocuments.getTokenFrequency().getOrDefault(term, 0) + 1;

            this.conditionalLogProbabilitiesSpam.put(term, Math.log((double)spamValue / (double)laplaceDenominatorSpam));
            this.conditionalLogProbabilitiesHam.put( term, Math.log((double)hamValue  / (double)laplaceDenominatorHam));
        }
    }

    private Set<String> getVocabulary(DocumentCollection spamDocuments, DocumentCollection hamDocuments) {
        Set<String> vocabulary = new HashSet<>();

        for(Map.Entry<String,Integer> entry : spamDocuments.getTokenFrequency().entrySet()) {
            String key = entry.getKey();
            vocabulary.add(key);
        }

        for(Map.Entry<String,Integer> entry : hamDocuments.getTokenFrequency().entrySet()) {
            String key = entry.getKey();
            vocabulary.add(key);
        }

        return vocabulary;
    }

    public Boolean isDocumentSpam(Document document) {
        Double spamValue = this.logProbabilitySpam;
        Double hamValue  = this.logProbabilityHam;

        for (Map.Entry<String,Integer> entry : document.getTokenFrequency().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (this.vocabulary.contains(key)) {
                spamValue += this.conditionalLogProbabilitiesSpam.get(key) * value;
                hamValue  += this.conditionalLogProbabilitiesHam.get(key)  * value;
            } else {
                // ignore this term
//                System.out.println("does not contain: " + key);
            }
        }

        if (spamValue > hamValue) return true;
        if (spamValue < hamValue) return false;
        if (spamValue.equals(hamValue)) System.out.println("EQUAL PROBABILITIES: assigning as spam");
        return true;
    }
}
