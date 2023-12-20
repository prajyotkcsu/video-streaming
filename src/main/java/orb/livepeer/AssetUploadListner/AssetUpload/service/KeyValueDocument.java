package orb.livepeer.AssetUploadListner.AssetUpload.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
public class KeyValueDocument {

    @Id
    private String id;

    private Map<String, String> keyValuePairs;

    public KeyValueDocument(String id) {
        this.id = id;
        this.keyValuePairs = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void put(String key, String value) {
        keyValuePairs.put(key, value);
    }
}
