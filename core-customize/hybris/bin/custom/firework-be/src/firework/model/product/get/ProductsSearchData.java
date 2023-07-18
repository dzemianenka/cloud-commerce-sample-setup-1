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
public class ProductsSearchData {
    @JsonProperty("page_number")
    private int pageNumber;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("total_entries")
    private long totalEntries;
    @JsonProperty("entries")
    private List<ProductSearchEntry> entries;
}
