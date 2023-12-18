package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class PlaybackDetails {
    private String playbackURL;
    private String mp4URL;
    private List<String> thumbNails;
    private String transcodingStatus;
}
