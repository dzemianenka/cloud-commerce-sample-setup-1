package firework.dto;

import de.hybris.platform.core.PK;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FireworkOAuthWsDTO {
    private PK pk;

    private String oAuthAppId;

    private String clientId;

    private String clientSecret;

    private String accessToken;

    private String refreshToken;

    private Date tokenExpiresIn;

    private Date refreshTokenExpiresIn;

    private String fireworkBusinessId;
}
