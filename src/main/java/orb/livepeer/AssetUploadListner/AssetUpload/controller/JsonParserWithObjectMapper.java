package orb.livepeer.AssetUploadListner.AssetUpload.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Status {
    private String phase;
    private Long updatedAt;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Snapshot {
    private String id;
    private String playbackId;
    private String userId;
    private Long createdAt;
    private Status status;
    private String name;
    private Source source;
    private String objectStoreId;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Source {
    private String type;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Asset {
    private String id;
    private Snapshot snapshot;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Payload {
    private Asset asset;

}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class WebhookEvent {
    private String id;
    private String webhookId;
    private Long createdAt;
    private Long timestamp;
    private String event;
    private Payload payload;

    // Getters and setters
}

public class JsonParserWithObjectMapper {
    public static String extractEventType(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        WebhookEvent webhookEvent = objectMapper.readValue(jsonString, WebhookEvent.class);
        return webhookEvent.getEvent();
    }
    public static AssetDetails extractAssetDetails(String jsonString) {
        try {
            String playbackID, assetId;

            ObjectMapper objectMapper = new ObjectMapper();
            WebhookEvent webhookEvent = objectMapper.readValue(jsonString, WebhookEvent.class);
            assetId=webhookEvent.getPayload().getAsset().getId();
            if (webhookEvent != null && webhookEvent.getPayload() != null && webhookEvent.getPayload().getAsset() != null) {
                playbackID= webhookEvent.getPayload().getAsset().getSnapshot().getPlaybackId();
            } else {
                return null; //throw an error
            }
            return AssetDetails.builder()
                    .assetId(assetId)
                    .playbackId(playbackID).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
