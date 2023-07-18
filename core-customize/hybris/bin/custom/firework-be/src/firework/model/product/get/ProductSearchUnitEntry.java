package firework.model.product.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchUnitEntry {
    @JsonProperty("unit_ext_id")
    private String unitExtId;
    @JsonProperty("unit_name")
    private String unitName;
    @JsonProperty("unit_options")
    private List<ProductSearchUnitOptionEntry> unitOptions;
    @JsonProperty("unit_position")
    private int unitPosition;
    @JsonProperty("unit_original_price")
    private String unitOriginalPrice;
    @JsonProperty("unit_price")
    private String unitPrice;
    @JsonProperty("unit_quantity")
    private long unitQuantity;
    @JsonProperty("unit_url")
    private String unitUrl;
}
