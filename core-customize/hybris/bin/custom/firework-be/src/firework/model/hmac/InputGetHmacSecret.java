package firework.model.hmac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InputGetHmacSecret(
        @JsonProperty("businessStoreId") String businessStoreId
) {
}
