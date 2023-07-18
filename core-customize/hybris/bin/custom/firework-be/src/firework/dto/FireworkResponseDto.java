package firework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FireworkResponseDto {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("contacts")
    private List contacts;

    @JsonProperty("id")
    private String id;

    @JsonProperty("redirect_uris")
    private List redirectUris;

    @JsonProperty("scope")
    private String scope;
}
