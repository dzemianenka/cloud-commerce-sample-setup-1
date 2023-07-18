package firework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FireworkAuthorizationResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_expires_in")
    private Long tokenExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;
}
