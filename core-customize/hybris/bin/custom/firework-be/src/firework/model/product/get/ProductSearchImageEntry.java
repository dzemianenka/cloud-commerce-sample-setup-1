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
public class ProductSearchImageEntry {
    @JsonProperty("image_ext_id")
    private String imageExtId;
    @JsonProperty("image_position")
    private int imagePosition;
    @JsonProperty("image_src")
    private String imageSrc;
    @JsonProperty("unit_identifiers")
    private List<String> unitIdentifiers;
    @JsonProperty("unit_names")
    private List<String> unitNames;

}
