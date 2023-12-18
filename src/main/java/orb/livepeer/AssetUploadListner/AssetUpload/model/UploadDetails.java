package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadDetails {
    private AssetDetails assetDetails;
    private PlaybackDetails playbackDetails;
    private boolean transcodingStatus;
}
