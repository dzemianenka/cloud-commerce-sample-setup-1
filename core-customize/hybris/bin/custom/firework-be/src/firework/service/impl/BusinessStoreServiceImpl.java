package firework.service.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import firework.client.BusinessStoreClient;
import firework.dto.FireworkCredentialsWsDTO;
import firework.model.businessstore.create.CreateBusinessStore;
import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.get.BusinessStore;
import firework.model.businessstore.update.InputUpdateBusinessStore;
import firework.model.businessstore.update.UpdateBusinessStore;
import firework.service.BusinessStoreService;
import firework.service.FwApiCredentialsService;
import firework.service.FwOAuthAppService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BusinessStoreServiceImpl implements BusinessStoreService {

    private final BusinessStoreClient businessStoreClient;

    private final FwOAuthAppService fwOAuthAppService;
    private final FwApiCredentialsService fwApiCredentialsService;
    private final BaseSiteService baseSiteService;

    @Override
    public List<BusinessStore> getBusinessStores() {
        return businessStoreClient.getBusinessStores().businessStores();
    }

    @Override
    public BusinessStore getBusinessStoreByStoreId(String businessStoreId) {

        return getBusinessStores().stream()
                .filter(businessStore -> businessStoreId.equals(businessStore.id()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Firework doesn't have the store with id = %s"
                        .formatted(businessStoreId)));
    }

    @Override
    public CreateBusinessStore createBusinessStore(InputCreateBusinessStore input) {
        final var businessStoreResponse = businessStoreClient.createBusinessStore(input);
        final var businessStore = businessStoreResponse.data().createBusinessStore();
        final var fwApiCredentials = FireworkCredentialsWsDTO.builder()
                .fireworkBusinessId(getBusinessId())
                .fireworkStoreId(businessStore.id())
                .storeUrl(businessStore.url())
                .baseSiteId(input.getName())
                .build();
        fwApiCredentialsService.saveFwApiCredentials(fwApiCredentials);
        return businessStore;
    }

    @Override
    public UpdateBusinessStore updateBusinessStore(InputUpdateBusinessStore input) {
        return businessStoreClient.updateBusinessStore(input).data().updateBusinessStore();
    }

    /// TODO for the removal
    // Logic:
    // 1) get business Stores from the firework
    // 2) get Base Sites from hybris that were used to add to firework
    // 3) for each base site create update to firework
    // 3.1) if hybris don't have information about base site delivery to firework,
    //      create entry in table fwapicredentials
    @Override
    public void syncBusinessStores() {
        final var businessStoresFromFirework = this.getBusinessStores();
        final var baseSitesFromFireWork = baseSiteService.getAllBaseSites().stream()
                .map(BaseSiteModel::getUid)
                .distinct()
                .filter(baseSiteId -> businessStoresFromFirework.stream()
                        .map(BusinessStore::name)
                        .anyMatch(businessStoreName -> businessStoreName.startsWith(baseSiteId)))
                .map(baseSiteService::getBaseSiteForUID)
                .toList();

        baseSitesFromFireWork.forEach(baseSiteModel -> {
            final var storeFromFirework = businessStoresFromFirework.stream()
                    .filter(businessStore -> businessStore.name().startsWith(baseSiteModel.getUid()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            final var currency = baseSiteModel.getStores().stream()
                    .map(BaseStoreModel::getDefaultCurrency)
                    .filter(Objects::nonNull)
                    .map(CurrencyModel::getIsocode)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            final var inputUpdateBusinessStore = InputUpdateBusinessStore.builder()
                    .storeId(storeFromFirework.id())
                    .storeName(baseSiteModel.getUid())
                    .currency(currency)
                    .build();
            final var updateBusinessStoreResult = updateBusinessStore(inputUpdateBusinessStore);
            final var fwApiCredentialsOpt = fwApiCredentialsService.getFwApiCredentialsOptByStoreId(storeFromFirework.id());
            if (fwApiCredentialsOpt.isEmpty()) {
                final var fwApiCredentials = FireworkCredentialsWsDTO.builder()
                        .fireworkBusinessId(getBusinessId())
                        .fireworkStoreId(updateBusinessStoreResult.id())
                        .storeUrl(updateBusinessStoreResult.url())
                        .baseSiteId(baseSiteModel.getUid())
                        .build();
                fwApiCredentialsService.saveFwApiCredentials(fwApiCredentials);
            }
        });

    }

    private String getBusinessId() {
        return fwOAuthAppService.getFwOAuthAppData().getFireworkBusinessId();
    }
}
