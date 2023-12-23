package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "upload-video")
public class UploadDetails {
    @Id
    @Field("_id")
    private ObjectId id;
    private String cid;
    private String assetId;
    private boolean livepeer;
    private String playbackId;
    private long timeModified;

    private String playbackURL;
    private List<MP4> source;
    private List<Thumbnail> thumbnails;
    private boolean transcodingCompleted;
}
