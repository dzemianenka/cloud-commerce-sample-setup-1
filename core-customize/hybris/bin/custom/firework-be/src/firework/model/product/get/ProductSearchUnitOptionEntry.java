package firework.model.product.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchUnitOptionEntry {

    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private String value;
}
