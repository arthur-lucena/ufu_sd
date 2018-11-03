import br.ufu.sd.work.model.Metadata;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.SerializationUtils.deserialize;

/**
 * Created by ismaley on 25/09/18.
 */
public class MetadataTest {

    private String message = "msg";
    private String createdBy = "process_create";
    private String updatedBy = "process_update";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now().plusMinutes(1);
    private Long id = 1L;

    private Metadata metadata = new Metadata(id, message, createdBy, createdAt, updatedBy, updatedAt);

    @Test
    public void should_serialize_and_deserialize() {
        byte[] serializedMetadata = SerializationUtils.serialize(metadata);
        Metadata deserializedMetadata = (Metadata) deserialize(serializedMetadata);

        Assert.assertEquals(id, deserializedMetadata.getId());
        Assert.assertEquals(message, deserializedMetadata.getMessage());
        Assert.assertEquals(createdBy, deserializedMetadata.getCreatedBy());
        Assert.assertEquals(createdAt, deserializedMetadata.getCreatedAt());
        Assert.assertEquals(updatedBy, deserializedMetadata.getUpdatedBy());
        Assert.assertEquals(updatedAt, deserializedMetadata.getUpdatedAt());

    }

}
