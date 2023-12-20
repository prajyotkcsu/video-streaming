package orb.livepeer.AssetUploadListner.AssetUpload.service;

import orb.livepeer.AssetUploadListner.AssetUpload.model.AssetDetails;
import orb.livepeer.AssetUploadListner.AssetUpload.model.UploadDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AssetRepository extends MongoRepository<UploadDetails, ObjectId> {
    @Query("{ 'assetId' : ?0 }")
    UploadDetails findByAssetId(String assetId);

}