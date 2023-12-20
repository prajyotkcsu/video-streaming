package orb.livepeer.AssetUploadListner.AssetUpload.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class VttReader {

    public static List<String> convertVttToJPEG(String vttFilePath) throws IOException {
        log.info("Converting vtt to images....");
        List<String> records = new ArrayList<>();
        URL urlObject = new URL(vttFilePath);
        String domain=vttFilePath.substring(0,vttFilePath.lastIndexOf('/'));
        // Open a connection to the URL
        URLConnection connection = urlObject.openConnection();

        // Create a BufferedReader to read the content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(".jpg")){
                    records.add(String.join("/",domain,line));
                }
            }
        }catch(Exception ex){
            log.error(String.valueOf(ex));
        }
        return records;
    }


}
