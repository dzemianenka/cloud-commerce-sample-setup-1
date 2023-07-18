package firework.model.businessstore.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateBusinessStoreResponse(
        UpdateBusinessStoreData data
) {
}
