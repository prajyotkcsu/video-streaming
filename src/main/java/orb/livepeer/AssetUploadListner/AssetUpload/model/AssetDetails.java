package orb.livepeer.AssetUploadListner.AssetUpload.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetDetails {
    private String assetId;
    private String playbackId;
}
