package firework.client;

import firework.mapper.BusinessStoreQueryMapper;
import firework.model.businessstore.create.CreateBusinessStoreResponse;
import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.get.GetBusinessStoresResponse;
import firework.model.businessstore.update.InputUpdateBusinessStore;
import firework.model.businessstore.update.UpdateBusinessStoreResponse;
import firework.model.hmac.BusinessStoreShuffleHmacSecretResponse;
import firework.service.FwOAuthAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
public class BusinessStoreClient {

    private final BusinessStoreQueryMapper businessStoreQueryMapper;
    private final RestTemplate restTemplate;
    private final String fireworkBaseUrl;
    private final FwOAuthAppService fwOAuthAppService;

    public GetBusinessStoresResponse getBusinessStores() {
        final HttpHeaders httpHeaders = getHeaders();
        return restTemplate.exchange(createGetUrl(), GET, new HttpEntity<>(httpHeaders), GetBusinessStoresResponse.class)
                .getBody();
    }

    public CreateBusinessStoreResponse createBusinessStore(InputCreateBusinessStore input) {
        final var query = businessStoreQueryMapper.mapToCreateBusinessStoreQuery(input);
        final var httpHeaders = getHeaders();

        return restTemplate.exchange(createGraphQlUrl(), POST, new HttpEntity<>(query, httpHeaders), CreateBusinessStoreResponse.class)
                .getBody();
    }

    public UpdateBusinessStoreResponse updateBusinessStore(InputUpdateBusinessStore input) {
        final var query = businessStoreQueryMapper.mapToUpdateBusinessStoreQuery(input);
        final var httpHeaders = getHeaders();

        return restTemplate.exchange(createGraphQlUrl(), POST, new HttpEntity<>(query, httpHeaders), UpdateBusinessStoreResponse.class)
                .getBody();
    }

    public BusinessStoreShuffleHmacSecretResponse getHmacSecret(String businessStoreId) {
        final var query = businessStoreQueryMapper.mapToGetHmacSecret(businessStoreId);
        final var httpHeaders = getHeaders();

        return restTemplate.exchange(createGraphQlUrl(), POST, new HttpEntity<>(query, httpHeaders), BusinessStoreShuffleHmacSecretResponse.class)
                .getBody();
    }

    private String createGraphQlUrl() {
        return fireworkBaseUrl + "/graphiql";
    }

    private HttpHeaders getHeaders() {
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(getAccessToken());
        return httpHeaders;
    }

    private String getAccessToken() {
        return fwOAuthAppService.getFwOAuthAppData().getAccessToken();
    }

    private String createGetUrl() {
        return fireworkBaseUrl + "/api/bus/%s/business_stores".formatted(getBusinessId());
    }

    private String getBusinessId() {
        return fwOAuthAppService.getFwOAuthAppData().getFireworkBusinessId();
    }
}
