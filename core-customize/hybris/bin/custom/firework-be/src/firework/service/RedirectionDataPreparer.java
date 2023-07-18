package firework.service;

import firework.dto.FireworkOAuthWsDTO;
import org.springframework.http.HttpEntity;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;

public interface RedirectionDataPreparer {
    RedirectView prepareAuthRedirectionData(FireworkOAuthWsDTO fwOAuthAppWsDTO, Boolean isOnboardingFlagEnabled);

    RedirectView preparePluginRedirectionData(Optional<FireworkOAuthWsDTO> fwOAuthAppWsDTO);

    Map<String, Object> prepareDataForTokenRequest(String code, FireworkOAuthWsDTO fromDb);

    HttpEntity<String> prepareHttpEntity(Map<String, Object> paramsToPreparationStep);

    Map<String, Object> prepareRegisterData();
}
