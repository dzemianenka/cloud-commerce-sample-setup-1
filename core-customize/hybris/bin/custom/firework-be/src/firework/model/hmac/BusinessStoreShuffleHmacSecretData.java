package firework.model.hmac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("data")
@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessStoreShuffleHmacSecretData(
        @JsonProperty("businessStoreShuffleHmacSecret") BusinessStoreShuffleHmacSecret businessStoreShuffleHmacSecret
) {
}
