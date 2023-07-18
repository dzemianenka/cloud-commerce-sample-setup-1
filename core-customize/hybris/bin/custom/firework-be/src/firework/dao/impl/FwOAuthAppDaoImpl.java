package firework.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import firework.dao.FwOAuthAppDao;
import firework.model.FwOAuthAppModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static firework.constants.FireworkConstants.GET_ALL_FWOAUTH_QUERY;
import static firework.constants.FireworkConstants.GET_BY_APP_ID_QUERY;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@RequiredArgsConstructor
public class FwOAuthAppDaoImpl implements FwOAuthAppDao {

    private static final Logger LOG = LoggerFactory.getLogger(FwOAuthAppDaoImpl.class);

    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;

    @Override
    public List<FwOAuthAppModel> getAllFwOAuthApp() {
        try {
            FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(GET_ALL_FWOAUTH_QUERY);
            SearchResult<FwOAuthAppModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);

            return Optional.ofNullable(searchResult)
                    .map(SearchResult::getResult)
                    .orElse(emptyList());
        } catch (FlexibleSearchException e) {
            LOG.error("Exception occurred while fetching the data from DB ", e);
            return emptyList();
        }
    }

    @Override
    public FwOAuthAppModel getFwOAuthAppByPk(PK pk) {
        return modelService.get(pk);
    }

    @Override
    public Optional<FwOAuthAppModel> getFwOAuthAppModelByOAuthAppId(String oAuthAppId) {
        try {
            FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(GET_BY_APP_ID_QUERY);
            flexibleSearchQuery.addQueryParameter("oAuthAppId", oAuthAppId);
            SearchResult<FwOAuthAppModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);

            return Optional.ofNullable(searchResult.getResult().get(0));
        } catch (FlexibleSearchException e) {
            LOG.error("Exception occurred while fetching the data from DB ", e);
            return empty();
        }
    }

    @Override
    public void saveFwOAuthAppModel(FwOAuthAppModel fwOAuthAppModel) {
        modelService.save(fwOAuthAppModel);
    }

    @Override
    public void updateFwOAuthApp(FwOAuthAppModel targetToUpdate) {
        modelService.save(targetToUpdate);
    }

    @Override
    public void deleteFwOAuthAppModel(FwOAuthAppModel targetToDelete) {
        modelService.removeAll(targetToDelete);
    }
}
