package firework.mapper.impl;

import firework.mapper.BusinessStoreQueryMapper;
import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.update.InputUpdateBusinessStore;
import firework.service.ConfigurationWatchService;
import firework.service.FwOAuthAppService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BusinessStoreQueryMapperImpl implements BusinessStoreQueryMapper {

    private final String provider;
    private final FwOAuthAppService fwOAuthAppService;
    private final ConfigurationWatchService configurationWatchService;

    @Override
    public String mapToCreateBusinessStoreQuery(InputCreateBusinessStore input) {
        return
                """
                          mutation {
                          createBusinessStore(createBusinessStoreInput:{businessId: "%s", currency:"%s", name:"%s Store", provider: "%s", uid:"%s", url:"%s", hostApiUrl:"%s"}) {
                            ... on BusinessStore {
                              id
                              name
                              provider
                              currency
                              url
                              accessToken
                            }
                            ... on AnyError {
                              message
                            }
                          }
                        }"""
                        .formatted(
                                getBusinessId(),
                                input.getCurrency(),
                                input.getName(),
                                getProvider(),
                                getUrl(input.getName()),
                                getUrl(input.getName()),
                                getHostApiUrl()
                        );
    }

    @Override
    public String mapToUpdateBusinessStoreQuery(InputUpdateBusinessStore input) {
        return """
                mutation {
                  updateBusinessStore(storeId: "%s", updateBusinessStoreInput:{ businessId: "%s", currency: "%s", hostApiUrl: "%s", name: "%s", uid: "%s", url: "%s"}) {
                    ... on BusinessStore {
                      id
                      name
                      provider
                      currency
                      url
                      accessToken
                		refreshToken
                		uid
                      business {
                        id
                      }
                    }
                    ... on AnyError {
                      message
                    }
                  }
                }
                """
                .formatted(
                        input.getStoreId(),
                        getBusinessId(),
                        input.getCurrency(),
                        getHostApiUrl(),
                        input.getStoreName(),
                        getUrl(input.getStoreName()),
                        getUrl(input.getStoreName())
                );
    }

    @Override
    public String mapToGetHmacSecret(String businessStoreId) {
        return """
                mutation {
                  businessStoreShuffleHmacSecret(businessStoreId: "%s"){
                  ... on BusinessStoreShuffleHmacSecretResult {hmacSecret}
                  ... on AnyError {message}
                  }
                }
                """.formatted(businessStoreId);
    }

    private String getHostApiUrl() {
        return configurationWatchService.getHostApiUrl();
    }

    private String getUrl(String baseSiteId) {
        return configurationWatchService.getBaseSiteUrl(baseSiteId);
    }

    private String getProvider() {
        return provider;
    }

    private String getBusinessId() {
        return fwOAuthAppService.getFwOAuthAppData().getFireworkBusinessId();
    }
}
