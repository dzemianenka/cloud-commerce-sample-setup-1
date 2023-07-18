package firework.model.businessstore.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("updateBusinessStore")
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateBusinessStore(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("business") Business business,
        @JsonProperty("currency") String currency,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("provider") String provider,
        @JsonProperty("refreshToken") String refreshToken,
        @JsonProperty("uid") String uid,
        @JsonProperty("url") String url
) {
}
