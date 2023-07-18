package firework.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import firework.dao.FwOAuthAppDao;
import firework.dto.FireworkOAuthWsDTO;
import firework.mapper.FwOAuthAppMapper;
import firework.service.FwOAuthAppService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FwOAuthAppServiceImpl implements FwOAuthAppService {

    private final FwOAuthAppDao fwOAuthAppDao;
    private final FwOAuthAppMapper fwOAuthAppMapper;

    @Override
    public List<FireworkOAuthWsDTO> getAllFwOAuthApp() {
        var fromDatabaseResult = fwOAuthAppDao.getAllFwOAuthApp();
        return fromDatabaseResult.stream()
                .map(fwOAuthAppMapper::toWsDTO)
                .toList();
    }


    @Override
    public FireworkOAuthWsDTO getFwOAuthAppByOAuthAppId(String oAuthAppId) {
        final var errorMessage = "Not found data for the business with id %s"
                .formatted(oAuthAppId);
        return fwOAuthAppDao.getFwOAuthAppModelByOAuthAppId(oAuthAppId)
                .map(fwOAuthAppMapper::toWsDTO)
                .orElseThrow(() -> new ModelNotFoundException(errorMessage));
    }

    @Override
    public FireworkOAuthWsDTO getFwOAuthAppData() {
        return getAllFwOAuthApp().stream().findAny()
                .orElseThrow(() -> new RuntimeException("Empty firework oauth application data!"));
    }

    @Override
    public void saveFwOAuthApp(FireworkOAuthWsDTO fwOAuthApp) {
        var targetToSave = fwOAuthAppMapper.toModel(fwOAuthApp);
        fwOAuthAppDao.saveFwOAuthAppModel(targetToSave);
    }

    @Override
    public void updateFwOAuthApp(FireworkOAuthWsDTO targetModel) {
        var targetFromDb = fwOAuthAppDao.getFwOAuthAppByPk(targetModel.getPk());
        var targetModelToUpdate = fwOAuthAppMapper.mergeToModel(targetFromDb, targetModel);

        fwOAuthAppDao.updateFwOAuthApp(targetModelToUpdate);
    }

    @Override
    public void deleteFwOAuthApp(FireworkOAuthWsDTO targetToDelete) {
        var targetFromDb = fwOAuthAppDao.getFwOAuthAppByPk(targetToDelete.getPk());
        fwOAuthAppDao.deleteFwOAuthAppModel(targetFromDb);
    }
}
