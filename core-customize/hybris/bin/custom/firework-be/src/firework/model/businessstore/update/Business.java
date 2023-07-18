package firework.model.businessstore.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("business")
@JsonIgnoreProperties(ignoreUnknown = true)
public record Business(
        @JsonProperty("id") String id
) {
}
