package firework.model.businessstore.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("data")
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateBusinessStoreData(
        UpdateBusinessStore updateBusinessStore
) {
}
