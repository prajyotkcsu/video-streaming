package orb.livepeer.AssetUploadListner.AssetUpload.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.PlaybackDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.service.VttReader;

import java.io.IOException;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Source{
    private String hrn;
    private String type;
    private String url;
    private Integer size;
    private Integer width;
    private Integer height;
    private Integer bitrate;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Meta {
    private String playbackPolicy;
    private List<Source> source;

    // Getters and setters
}
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class VideoMetadata {
    private String type;
    private Meta meta;

    public PlaybackDetails extractVideoList(VideoMetadata videoMetadata) throws IOException {

        List<Source> sourceList=videoMetadata.getMeta().getSource();
        VttReader vttReader=new VttReader();
        List<String> thumbNails =vttReader.convertVttToJPEG(sourceList.get(2).getUrl());
        log.info("thumbNails********{}",thumbNails.toString());
        return PlaybackDetails.builder()
                .mp4URL(sourceList.get(0).getUrl())
                .playbackURL(sourceList.get(1).getUrl())
                .thumbNails(thumbNails)
                .build();
    }
}


