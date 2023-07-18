package firework.model.hmac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("businessStoreShuffleHmacSecret")
@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessStoreShuffleHmacSecret(
        @JsonProperty("hmacSecret") String hmacSecret
) {
}
