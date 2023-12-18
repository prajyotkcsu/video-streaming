package orb.livepeer.AssetUploadListner.AssetUpload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssetUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetUploadApplication.class, args);
		System.out.println("Hello from AssetUpload service");
	}

}
