package firework.model.businessstore.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBusinessStoreResponse(
        @JsonProperty("data") CreateBusinessStoreData data
) {
}
