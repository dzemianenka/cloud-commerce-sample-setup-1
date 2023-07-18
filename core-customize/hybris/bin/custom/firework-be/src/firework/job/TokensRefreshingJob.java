package firework.job;

import com.google.gson.Gson;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import firework.dto.FireworkAuthorizationResponseDto;
import firework.dto.FireworkOAuthWsDTO;
import firework.service.FwOAuthAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static java.util.Objects.nonNull;

public class TokensRefreshingJob extends AbstractJobPerformable<CronJobModel> {

    @Value("${firework.api.url.oauth.token}")
    private String fireworkTokenUrl;

    @Value("${firework.api.url.extensionUrl}")
    private String extensionUrl;

    @Value("${firework.api.url.callbackUrlSuffix}")
    private String callbackUrlSuffix;

    private final FwOAuthAppService fwOAuthAppService;
    private final RestTemplate restTemplate;

    public TokensRefreshingJob(FwOAuthAppService fwOAuthAppService, RestTemplate restTemplate) {
        this.fwOAuthAppService = fwOAuthAppService;
        this.restTemplate = restTemplate;
    }

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        checkAndUpdateIfTokenExpired();

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private void checkAndUpdateIfTokenExpired() {
        var targetToCheck = fwOAuthAppService.getFwOAuthAppData();

        var refreshToken = targetToCheck.getRefreshToken();
        var expirationDate = targetToCheck.getTokenExpiresIn();
        var currentDate = new Date();

        if (nonNull(refreshToken) && currentDate.after(expirationDate)) {
            var response = performRefreshTokenRequest(targetToCheck);
            var actualTokenData = response.getBody();

            updateTokenData(targetToCheck, actualTokenData);
        }

    }

    private void updateTokenData(FireworkOAuthWsDTO targetToCheck, FireworkAuthorizationResponseDto actualTokenData) {
        targetToCheck.setAccessToken(actualTokenData.getAccessToken());
        targetToCheck.setRefreshToken(actualTokenData.getRefreshToken());

        var createdAt = actualTokenData.getCreatedAt();
        var tokenExpirationPeriod = actualTokenData.getTokenExpiresIn();
        var refreshTokenExpirationPeriod = actualTokenData.getRefreshTokenExpiresIn();

        targetToCheck.setTokenExpiresIn(this.createTokenExpirationDate(createdAt, tokenExpirationPeriod));
        targetToCheck.setRefreshTokenExpiresIn(this.createTokenExpirationDate(createdAt, refreshTokenExpirationPeriod));

        fwOAuthAppService.updateFwOAuthApp(targetToCheck);
    }

    private Date createTokenExpirationDate(Date createdAt, Long expirationPeriod) {
        long expirationPeriodInMillis = ofSeconds(expirationPeriod).toMillis();
        return new Date(createdAt.getTime() + expirationPeriodInMillis);
    }

    public ResponseEntity<FireworkAuthorizationResponseDto> performRefreshTokenRequest(FireworkOAuthWsDTO fromDb) {
        var paramsToPreparationStep = prepareRefreshTokenData(fromDb);
        var httpEntity = prepareHttpEntity(paramsToPreparationStep);

        return restTemplate.postForEntity(fireworkTokenUrl, httpEntity, FireworkAuthorizationResponseDto.class);
    }

    private Map<String, Object> prepareRefreshTokenData(FireworkOAuthWsDTO fromDb) {
        return Map.of("grant_type", "refresh_token",
                "redirect_uri", extensionUrl + callbackUrlSuffix,
                "client_id", fromDb.getClientId(),
                "refresh_token", fromDb.getRefreshToken());
    }

    private HttpEntity<String> prepareHttpEntity(Map<String, Object> params) {
        var headers = new HttpHeaders();
        var gson = new Gson();
        var json = gson.toJson(params);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(json, headers);
    }
}
