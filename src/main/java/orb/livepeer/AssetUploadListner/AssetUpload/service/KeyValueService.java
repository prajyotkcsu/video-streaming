package orb.livepeer.AssetUploadListner.AssetUpload.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class KeyValueService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public KeyValueService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void storeOrUpdate(String id, String key, String value) {
        KeyValueDocument keyValueDocument = mongoTemplate.findById(id, KeyValueDocument.class);

        if (keyValueDocument == null) {
            // If the document doesn't exist, create a new one
            keyValueDocument = new KeyValueDocument(id);
        }

        // Update or append the key-value pair
        keyValueDocument.put(key, value);

        // Save the document back to the database
        mongoTemplate.save(keyValueDocument);
    }
}
