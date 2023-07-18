package firework.dto;

import de.hybris.platform.core.PK;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FireworkCredentialsWsDTO {

    private PK pk;
    private String fireworkBusinessId;

    private String fireworkStoreId;

    private String baseSiteId;

    private String storeUrl;

    private String hmacSigningKey;
}
