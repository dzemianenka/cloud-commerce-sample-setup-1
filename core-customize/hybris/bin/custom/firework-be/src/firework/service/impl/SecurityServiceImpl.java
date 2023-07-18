package firework.service.impl;

import firework.client.BusinessStoreClient;
import firework.service.FwApiCredentialsService;
import firework.service.SecurityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final BusinessStoreClient businessStoreClient;
    private final FwApiCredentialsService fwApiCredentialsService;

    @Override
    public String getHmacSecret(String businessStoreId) {
        final var hmacSecretResponse = businessStoreClient.getHmacSecret(businessStoreId);
        final var fwApiCredentials = fwApiCredentialsService.getFwApiCredentialsByStoreId(businessStoreId);
        fwApiCredentials.setHmacSigningKey(hmacSecretResponse.businessStoreShuffleHmacSecretData().businessStoreShuffleHmacSecret().hmacSecret());
        fwApiCredentialsService.updateFwApiCredentials(fwApiCredentials);
        return hmacSecretResponse
                .businessStoreShuffleHmacSecretData()
                .businessStoreShuffleHmacSecret()
                .hmacSecret();
    }
}
