package orb.livepeer.AssetUploadListner.AssetUpload.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.MP4;
import orb.livepeer.AssetUploadListner.AssetUpload.model.PlaybackDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.Thumbnail;
import orb.livepeer.AssetUploadListner.AssetUpload.service.VttReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<MP4> source=new ArrayList<>();
        for(Source sourcel: sourceList){
            if(sourcel.getUrl().endsWith(".mp4")){
                log.info("inside mp4");
                MP4 mp4=MP4.builder()
                        .hrn(sourcel.getHrn())
                        .type("video/mp4")
                        .url(sourcel.getUrl())
                        .size(sourcel.getSize())
                        .width(sourcel.getWidth())
                        .height(sourcel.getHeight())
                        .bitrate(sourcel.getBitrate())
                        .build();
                source.add(mp4);
                playbackDetails.setSource(source);
            }
            else if(sourcel.getUrl().endsWith(".m3u8")){
                log.info("inside m3u8");
                playbackDetails.setPlaybackURL(sourcel.getUrl());}
            else if(sourcel.getUrl().endsWith(".vtt")){
                log.info("inside vtt");
                playbackDetails.setVttURL(sourcel.getUrl());}
        }
        VttReader vttReader=new VttReader();
        log.info("New video");
        List<Thumbnail> thumbNails;
        if(playbackDetails.getVttURL()!=null){
            thumbNails =vttReader.convertVttToJPEG(playbackDetails.getVttURL());
            playbackDetails.setThumbnails(thumbNails);
        }
        else
            playbackDetails.setThumbnails(null);
        log.info("***thumbNails produced***");
        return playbackDetails;
    }
}


