package firework.model.businessstore.get;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("payment_settings")
public record PaymentSettings(
        @JsonProperty("fast_app_id") String fastAppId,
        @JsonProperty("fast_enabled") Boolean fastEnabled) {
}
