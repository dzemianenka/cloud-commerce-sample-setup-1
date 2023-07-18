package firework.dao;

import de.hybris.platform.core.PK;
import firework.model.FwApiCredentialsModel;

import java.util.List;
import java.util.Optional;

public interface FwApiCredentialsDao {
    List<FwApiCredentialsModel> getAllFwApiCredentials();

    Optional<FwApiCredentialsModel> getFwApiCredentialsByStoreId(String storeId);

    FwApiCredentialsModel getFwApiCredentialsByPk(PK pk);

    void saveFwApiCredentialsModel(FwApiCredentialsModel fwApiCredentialsModel);

    void updateFwApiCredentialsModel(FwApiCredentialsModel fwApiCredentialsModel);
}
