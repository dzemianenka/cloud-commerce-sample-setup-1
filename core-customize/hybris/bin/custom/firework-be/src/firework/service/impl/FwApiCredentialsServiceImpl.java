package firework.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import firework.dao.FwApiCredentialsDao;
import firework.dto.FireworkCredentialsWsDTO;
import firework.mapper.FwApiCredentialsMapper;
import firework.service.FwApiCredentialsService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FwApiCredentialsServiceImpl implements FwApiCredentialsService {

    private final FwApiCredentialsDao fwApiCredentialsDao;
    private final FwApiCredentialsMapper fwApiCredentialsMapper;

    @Override
    public List<FireworkCredentialsWsDTO> getAllFwApiCredentials() {
        var fromDatabaseResult = fwApiCredentialsDao.getAllFwApiCredentials();
        return fromDatabaseResult.stream()
                .map(fwApiCredentialsMapper::toWsDTO)
                .toList();
    }

    @Override
    public FireworkCredentialsWsDTO getFwApiCredentialsByStoreId(String storeId) {
        final var errorMessage = "Not found data for the business store with id %s"
                .formatted(storeId);
        return fwApiCredentialsDao.getFwApiCredentialsByStoreId(storeId)
                .map(fwApiCredentialsMapper::toWsDTO)
                .orElseThrow(() -> new ModelNotFoundException(errorMessage));
    }

    @Override
    public Optional<FireworkCredentialsWsDTO> getFwApiCredentialsOptByStoreId(String storeId) {
        return fwApiCredentialsDao.getFwApiCredentialsByStoreId(storeId)
                .map(fwApiCredentialsMapper::toWsDTO);
    }

    @Override
    public void saveFwApiCredentials(FireworkCredentialsWsDTO fwApiCredentials) {
        var targetToSave = fwApiCredentialsMapper.toModel(fwApiCredentials);
        fwApiCredentialsDao.saveFwApiCredentialsModel(targetToSave);
    }

    @Override
    public void updateFwApiCredentials(FireworkCredentialsWsDTO fwApiCredentials) {
        final var targetFromDb = fwApiCredentialsDao.getFwApiCredentialsByPk(fwApiCredentials.getPk());
        final var targetToUpdate = fwApiCredentialsMapper.mergeToModel(targetFromDb, fwApiCredentials);
        fwApiCredentialsDao.updateFwApiCredentialsModel(targetToUpdate);
    }
}
