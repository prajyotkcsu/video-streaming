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

@RestController
@Slf4j
public class AssetUploadController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AssetRepository assetRepository;
    @PostMapping("/asset-ready")
    public ResponseEntity<UploadDetails> assetReadyNotification(@RequestBody String assetNotification) throws IOException {
        log.info("Received asset notification:{} ", assetNotification);
        ObjectMapper objectMapper = new ObjectMapper();
        AssetDetails assetDetails = null;
        PlaybackDetails playbackDetails = null;
        UploadDetails uploadDetails=null;
        // Parse the JSON string into a JsonNode
        String eventType = objectMapper.readTree(assetNotification).get("event").asText();

        if (eventType.equals("asset.created")) {
            log.info("Asset uploading........");
            assetDetails = extractAssetDetails(assetNotification);
            log.info("PlaybackId:{} ", assetDetails);
            assert assetDetails != null;
            ObjectId objectId = new ObjectId();
            assetRepository.save(new UploadDetails(UUID.randomUUID().toString(),assetDetails.getAssetId(),assetDetails.getPlaybackId()));
            log.info("Transcoding in progress........");
        } else if (eventType.equals("asset.ready")) {
            log.info("Asset uploaded{}",assetNotification);
            String assetId=objectMapper.readTree(assetNotification).get("payload").get("id").asText();
            UploadDetails asset=assetRepository.findByAssetId(assetId);
            log.info("Fetching video details......");
            VideoMetadata videoMetadata = restTemplate.exchange("https://livepeer.studio/api/playback/"+asset.getPlaybackId(), HttpMethod.GET, null, VideoMetadata.class).getBody();
            assert videoMetadata != null;
            playbackDetails = videoMetadata.extractVideoList(videoMetadata);
            uploadDetails = UploadDetails.builder()
                    .id(asset.getId())
                    .assetId(asset.getAssetId())
                    .livepeer(true)
                    .playbackId(asset.getPlaybackId())
                    .mp4URL(playbackDetails.getMp4URL())
                    .playbackURL(playbackDetails.getPlaybackURL())
                    .thumbNails(playbackDetails.getThumbNails())
                    .transcodingCompleted(true)
                    .timestamp(System.currentTimeMillis())
                    .build();
            log.info("Asset uploaded!!!");
            log.info("Upload Details: {}",uploadDetails);
            assetRepository.save(uploadDetails);
        }

    return ResponseEntity.ok(uploadDetails);
    }
}

