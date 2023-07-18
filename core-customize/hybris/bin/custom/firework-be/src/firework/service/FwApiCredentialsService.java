package firework.service;

import firework.dto.FireworkCredentialsWsDTO;

import java.util.List;
import java.util.Optional;

public interface FwApiCredentialsService {
    List<FireworkCredentialsWsDTO> getAllFwApiCredentials();

    FireworkCredentialsWsDTO getFwApiCredentialsByStoreId(String storeId);

    Optional<FireworkCredentialsWsDTO> getFwApiCredentialsOptByStoreId(String storeId);

    void saveFwApiCredentials(FireworkCredentialsWsDTO fwApiCredentials);

    void updateFwApiCredentials(FireworkCredentialsWsDTO fwApiCredentialsWsDTO);
}
