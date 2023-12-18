package orb.livepeer.AssetUploadListner.AssetUpload.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.PlaybackDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.UploadDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.video.VideoMetadata;
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

import static orb.livepeer.AssetUploadListner.AssetUpload.controller.JsonParserWithObjectMapper.*;

@RestController
@Slf4j
public class AssetUploadController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/")
    public String greet(){
        log.info("Into the void again");
        return "Hello PK updated";
    }
    @GetMapping("/publish/asset-upload")
    public String assetUpload(){
        log.info("New video uploaded!!");
        return "Asset has been uploaded!! Check it out";
    }


    @PostMapping("/asset-ready")
    public ResponseEntity<UploadDetails> assetReadyNotification(@RequestBody String assetNotification) throws IOException {
        log.info("Received asset notification:{} ", assetNotification);
        //if id present, possibly asset.ready
        //update MongoDb with id, playbackId, thumbNail, mp4Link,
        ObjectMapper objectMapper = new ObjectMapper();
        AssetDetails assetDetails = null;
        PlaybackDetails playbackDetails=null;
        // Parse the JSON string into a JsonNode
        String eventType = objectMapper.readTree(assetNotification).get("event").asText();

        if (eventType.equals("asset.created")) {
            assetDetails = extractAssetDetails(assetNotification);
            log.info("PlaybackId:{} ", assetDetails);
        } else if (eventType.equals("asset.ready")) {
            VideoMetadata videoMetadata=restTemplate.exchange("https://livepeer.studio/api/playback/09ee1iihioqf4zon", HttpMethod.GET,null, VideoMetadata.class).getBody();
            assert videoMetadata != null;
            playbackDetails=videoMetadata.extractVideoList(videoMetadata);
        }
        UploadDetails uploadDetails=UploadDetails.builder()
                .assetDetails(assetDetails)
                .playbackDetails(playbackDetails)
                .transcodingStatus(true)
                .build();
        log.info("Asset uploaded successfully:{}",uploadDetails);
        return ResponseEntity.ok(uploadDetails);
    }
}

