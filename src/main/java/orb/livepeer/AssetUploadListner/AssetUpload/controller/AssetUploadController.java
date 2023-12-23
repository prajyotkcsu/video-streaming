package orb.livepeer.AssetUploadListner.AssetUpload.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.PlaybackDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.UploadDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.service.AssetRepository;
import orb.livepeer.AssetUploadListner.AssetUpload.video.VideoMetadata;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static orb.livepeer.AssetUploadListner.AssetUpload.controller.JsonParserWithObjectMapper.*;
import static org.springframework.data.mongodb.repository.Query.*;

@RestController
@Slf4j
public class AssetUploadController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @PostMapping("/asset-ready")
    public ResponseEntity<UploadDetails> assetReadyNotification(@RequestBody String assetNotification) throws IOException {
        log.info("Received asset notification:{} ", assetNotification);
        ObjectMapper objectMapper = new ObjectMapper();
        AssetDetails assetDetails = null;
        PlaybackDetails playbackDetails = null;
        UploadDetails uploadDetails=null;
        // Parse the JSON string into a JsonNode

        String eventType = objectMapper.readTree(assetNotification).get("event").asText();
        UploadDetails asset = null;
        if (eventType.equals("asset.ready")) {
            log.info("Asset uploaded{}", assetNotification);
            String assetId = objectMapper.readTree(assetNotification).get("payload").get("id").asText();
            asset = assetRepository.findByAssetId(assetId);
            if(asset==null){
                System.out.println("Asset not found");
                return null;
            }
            log.info("Fetching video details......");
            VideoMetadata videoMetadata = restTemplate.exchange("https://livepeer.studio/api/playback/" + asset.getPlaybackId(), HttpMethod.GET, null, VideoMetadata.class).getBody();
            assert videoMetadata != null;
            playbackDetails = videoMetadata.extractVideoList(videoMetadata);
            asset.setSource(playbackDetails.getSource());
            asset.setPlaybackURL(playbackDetails.getPlaybackURL());
            asset.setThumbnails(playbackDetails.getThumbnails());
            asset.setTranscodingCompleted(true);
            asset.setTimeModified(System.currentTimeMillis());
            log.info("Asset ready: {}", asset);
            assetRepository.save(asset);
            log.info("Asset uploaded!!!");
        }

    return ResponseEntity.ok(asset);
    }
}