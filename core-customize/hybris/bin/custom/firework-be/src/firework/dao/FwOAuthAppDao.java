package firework.dao;

import de.hybris.platform.core.PK;
import firework.model.FwOAuthAppModel;

import java.util.List;
import java.util.Optional;

public interface FwOAuthAppDao {

    List<FwOAuthAppModel> getAllFwOAuthApp();

    FwOAuthAppModel getFwOAuthAppByPk(PK pk);

    void updateFwOAuthApp(FwOAuthAppModel targetToUpdate);

    Optional<FwOAuthAppModel> getFwOAuthAppModelByOAuthAppId(String oAuthAppId);

    void saveFwOAuthAppModel(FwOAuthAppModel fwOAuthAppModel);

    void deleteFwOAuthAppModel(FwOAuthAppModel targetToDelete);
}
