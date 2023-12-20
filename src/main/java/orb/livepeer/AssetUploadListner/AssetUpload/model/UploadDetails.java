package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "upload-video")
public class UploadDetails {
    @Id
    private String id;
    private String assetId;
    private boolean livepeer;
    private String playbackId;
    private String playbackURL;
    private String mp4URL;
    private List<String> thumbNails;
    private boolean transcodingCompleted;
    private long timestamp;

    public UploadDetails(String id,String assetId, String playbackId) {
        this.id = id;
        this.assetId = assetId;
        this.playbackId = playbackId;
    }
}
