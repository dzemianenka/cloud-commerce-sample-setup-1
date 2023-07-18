package firework.model.businessstore.get;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public record BusinessStore(
        @JsonProperty("business_id") String businessId,
        @JsonProperty("currency") String currency,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("payment_settings") PaymentSettings paymentSettings,
        @JsonProperty("provider") String provider,
        @JsonProperty("uid") String uid,
        @JsonProperty("url") String url) {
}
