package firework.model.businessstore.get;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record GetBusinessStoresResponse(
        @JsonProperty("business_stores") List<BusinessStore> businessStores) {
}
