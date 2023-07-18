package firework.model.product.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchEntry {
    @JsonProperty("product_ext_id")
    private String productExtId;
    @JsonProperty("product_currency")
    private String productCurrency;
    @JsonProperty("product_handle")
    private String productHandle;
    @JsonProperty("business_store_name")
    private String businessStoreName;
    @JsonProperty("business_store_id")
    private String businessStoreId;
    @JsonProperty("business_store_uid")
    private String businessStoreUid;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_description")
    private String productDescription;
    @JsonProperty("product_options")
    private Set<String> productOptions;
    @JsonProperty("product_images")
    private List<ProductSearchImageEntry> productImages;
    @JsonProperty("product_units")
    private List<ProductSearchUnitEntry> productUnits;

}
