package firework.client;

import firework.dto.FireworkAuthorizationResponseDto;
import firework.dto.FireworkOAuthWsDTO;
import firework.dto.FireworkResponseDto;
import firework.service.RedirectionDataPreparer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class FireworkOAuthClient {

    private final RestTemplate restTemplate;
    private final RedirectionDataPreparer redirectionDataPreparer;

    @Value("${firework.api.url.oauth.token}")
    public String fireworkTokenUrl;
    @Value("${firework.api.url.oauth.register}")
    public String fireworkRegisterUrl;

    public ResponseEntity<FireworkAuthorizationResponseDto> performTokenRetrievingRequest(String code, FireworkOAuthWsDTO fromDb) {
        var paramsToPreparationStep = redirectionDataPreparer.prepareDataForTokenRequest(code, fromDb);
        var httpEntity = redirectionDataPreparer.prepareHttpEntity(paramsToPreparationStep);

        return restTemplate.postForEntity(
                fireworkTokenUrl,
                httpEntity,
                FireworkAuthorizationResponseDto.class);
    }

    public ResponseEntity<FireworkResponseDto> performExternalRegistrationRequest() {
        var paramsToPreparationStep = redirectionDataPreparer.prepareRegisterData();
        var httpEntity = redirectionDataPreparer.prepareHttpEntity(paramsToPreparationStep);

        return restTemplate.postForEntity(
                fireworkRegisterUrl,
                httpEntity,
                FireworkResponseDto.class);
    }

    private FireworkResponseDto performRequest(HttpEntity<String> httpEntity) {
        var response = restTemplate.postForEntity(
                fireworkRegisterUrl,
                httpEntity,
                FireworkResponseDto.class);

        return response.getBody();
    }
}
