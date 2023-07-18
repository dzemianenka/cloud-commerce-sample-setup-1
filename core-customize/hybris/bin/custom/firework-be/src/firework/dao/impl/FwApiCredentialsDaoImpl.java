package firework.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import firework.dao.FwApiCredentialsDao;
import firework.model.FwApiCredentialsModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static firework.constants.FireworkConstants.GET_ALL_FWAPI_QUERY;
import static firework.constants.FireworkConstants.GET_BY_STORE_ID_QUERY;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@RequiredArgsConstructor
public class FwApiCredentialsDaoImpl implements FwApiCredentialsDao {

    private static final Logger LOG = LoggerFactory.getLogger(FwApiCredentialsDaoImpl.class);

    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;

    @Override
    public List<FwApiCredentialsModel> getAllFwApiCredentials() {

        try {
            FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(GET_ALL_FWAPI_QUERY);
            SearchResult<FwApiCredentialsModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);

            return Optional.ofNullable(searchResult)
                    .map(SearchResult::getResult)
                    .orElse(emptyList());
        } catch (FlexibleSearchException e) {
            LOG.error("Exception occurred while fetching the data from DB ", e);
            return emptyList();
        }
    }

    @Override
    public Optional<FwApiCredentialsModel> getFwApiCredentialsByStoreId(String storeId) {
        try {
            FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(GET_BY_STORE_ID_QUERY);
            flexibleSearchQuery.addQueryParameter("fireworkStoreId", storeId);
            SearchResult<FwApiCredentialsModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);

            return searchResult.getResult().stream().findAny();
        } catch (FlexibleSearchException e) {
            LOG.error("Exception occurred while fetching the data from DB ", e);
            return empty();
        }
    }

    @Override
    public FwApiCredentialsModel getFwApiCredentialsByPk(PK pk) {
        return modelService.get(pk);
    }

    @Override
    public void saveFwApiCredentialsModel(FwApiCredentialsModel fwApiCredentialsModel) {
        modelService.save(fwApiCredentialsModel);
    }

    @Override
    public void updateFwApiCredentialsModel(FwApiCredentialsModel fwApiCredentialsModel) {

        modelService.save(fwApiCredentialsModel);
    }
}
