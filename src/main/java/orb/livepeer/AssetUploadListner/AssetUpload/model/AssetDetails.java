package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetDetails {
    private String assetId;
    private String playbackId;
    private String playbackURL;
    private String mp4VideoLink;
    private List<String> thumbNails;
    private String transcodingStatus;
}
