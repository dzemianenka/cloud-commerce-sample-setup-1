package firework.model.businessstore.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputUpdateBusinessStore {
    private String storeId;
    private String currency;
    private String storeName;
}
