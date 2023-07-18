package firework.service;

import firework.dto.FireworkOAuthWsDTO;

import java.util.List;

public interface FwOAuthAppService {
    List<FireworkOAuthWsDTO> getAllFwOAuthApp();

    FireworkOAuthWsDTO getFwOAuthAppByOAuthAppId(String oAuthAppId);

    void saveFwOAuthApp(FireworkOAuthWsDTO fwOAuthApp);

    FireworkOAuthWsDTO getFwOAuthAppData();

    void updateFwOAuthApp(FireworkOAuthWsDTO fromDb);

    void deleteFwOAuthApp(FireworkOAuthWsDTO targetToDelete);
}
