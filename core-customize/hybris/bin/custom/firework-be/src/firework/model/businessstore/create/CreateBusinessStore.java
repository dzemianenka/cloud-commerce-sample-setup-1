package firework.model.businessstore.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("createBusinessStore")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBusinessStore(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("currency") String currency,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("provider") String provider,
        @JsonProperty("url") String url
) {
}
