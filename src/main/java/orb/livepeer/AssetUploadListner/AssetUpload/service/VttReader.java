package orb.livepeer.AssetUploadListner.AssetUpload.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VttReader {

    public static List<String> convertVttToJPEG(String vttFilePath) throws IOException {
        List<String> records = new ArrayList<>();
        try (BufferedReader vttReader = new BufferedReader(new FileReader(vttFilePath))) {
            String line;
            while ((line = vttReader.readLine()) != null) {
                // Add each line (image file name) to the records
                records.add(line.trim());
            }
        }
        return records;
    }


}
