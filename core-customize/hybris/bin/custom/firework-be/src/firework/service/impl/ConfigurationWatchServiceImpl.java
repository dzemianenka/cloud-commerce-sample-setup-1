package firework.service.impl;


import de.hybris.platform.servicelayer.config.ConfigurationService;
import firework.service.ConfigurationWatchService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationWatchServiceImpl implements ConfigurationWatchService {

    private static final String HOST_API_URL_PARAM_NAME = "ccv2.services.api.url.0";
    private static final String STOREFRONT_SITE_URL_PARAM_NAME = "website.%s.https";

    private final ConfigurationService configurationService;

    @Override
    public String getHostApiUrl() {
        return configurationService.getConfiguration().getString(HOST_API_URL_PARAM_NAME);
    }

    @Override
    public String getBaseSiteUrl(String baseSiteId) {
        return configurationService.getConfiguration().getString(STOREFRONT_SITE_URL_PARAM_NAME.formatted(baseSiteId));
    }
}
