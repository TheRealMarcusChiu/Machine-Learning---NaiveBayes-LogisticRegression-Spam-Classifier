package util;

import lombok.Data;

import java.util.*;

@Data
public class DocumentCollection {

    Integer numDocuments;
    Integer totalTokens;
    Map<String, Integer> tokenFrequency;

    public DocumentCollection(ArrayList<Document> documents) {
        this.numDocuments = documents.size();
        this.totalTokens = 0;
        this.tokenFrequency = new TreeMap<>();

        for (Document d : documents) {
            this.addDocument(d);
        }
    }

    private void addDocument(Document document) {
        this.totalTokens += document.totalTokens;

        for(Map.Entry<String,Integer> entry : document.tokenFrequency.entrySet()) {

            String key = entry.getKey();
            Integer count = entry.getValue();

            if (!this.tokenFrequency.containsKey(key)) {
                this.tokenFrequency.put(key, count);
            } else {
                this.tokenFrequency.put(key, this.tokenFrequency.get(key) + count);
            }
        }
    }

    public static Set<String> getVocabulary(DocumentCollection spamDocuments, DocumentCollection hamDocuments) {
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
}
