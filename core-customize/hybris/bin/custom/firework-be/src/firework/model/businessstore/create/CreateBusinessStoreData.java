package firework.model.businessstore.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("data")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBusinessStoreData(
        @JsonProperty("createBusinessStore") CreateBusinessStore createBusinessStore
) {
}
