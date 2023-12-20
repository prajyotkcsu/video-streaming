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
        log.info("Fetching video thumbnails....");
        PlaybackDetails playbackDetails=new PlaybackDetails();
        List<Source> sourceList=videoMetadata.getMeta().getSource();
        log.info("sourceList:{} ",sourceList);
        for(Source source: sourceList){
            if(source.getUrl().endsWith(".mp4")){
                log.info("inside mp4");
                playbackDetails.setMp4URL(source.getUrl());}
            else if(source.getUrl().endsWith(".m3u8")){
                log.info("inside m3u8");
                playbackDetails.setPlaybackURL(source.getUrl());}
            else if(source.getUrl().endsWith(".vtt")){
                log.info("inside vtt");
                playbackDetails.setVttURL(source.getUrl());}
        }
        VttReader vttReader=new VttReader();
        log.info("New video");
        List<String> thumbNails;
        if(playbackDetails.getVttURL()!=null){
            thumbNails =vttReader.convertVttToJPEG(playbackDetails.getVttURL());
            playbackDetails.setThumbNails(thumbNails);
        }
        else
            playbackDetails.setThumbNails(null);
        log.info("***thumbNails produced***");
        return playbackDetails;
    }
}


