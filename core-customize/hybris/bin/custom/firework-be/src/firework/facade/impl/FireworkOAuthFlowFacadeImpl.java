package firework.facade.impl;

import firework.client.FireworkOAuthClient;
import firework.dto.FireworkOAuthWsDTO;
import firework.facade.FireworkOAuthFlowFacade;
import firework.mapper.FwOAuthAppMapper;
import firework.service.BusinessStoreService;
import firework.service.FwOAuthAppService;
import firework.service.RedirectionDataPreparer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.of;
import static org.fest.util.Collections.isEmpty;

@RequiredArgsConstructor
public class FireworkOAuthFlowFacadeImpl implements FireworkOAuthFlowFacade {

    private final FireworkOAuthClient fireworkOAuthClient;
    private final FwOAuthAppService fwOAuthAppService;
    private final RedirectionDataPreparer redirectionDataPreparer;
    private final FwOAuthAppMapper fwOAuthAppMapper;
    private final BusinessStoreService businessStoreService;

    @Override
    public RedirectView register() {
        var fromDatabase = fwOAuthAppService.getAllFwOAuthApp();

        if (isEmpty(fromDatabase)) {
            return performOAuthFlowWithOnboarding();
        } else if (isPresentSingleInvalidOAuthItem(fromDatabase)) {
            return performOAuthFlowWithoutOnboarding(fromDatabase);
        } else {
            return checkRefreshTokenAndRedirect(fromDatabase.get(0));
        }
    }

    @Override
    public RedirectView callbackToCodeHandling(String code, String state, String subBusinessId) {
        var fromDb = fwOAuthAppService.getFwOAuthAppByOAuthAppId(state);
        var response = fireworkOAuthClient.performTokenRetrievingRequest(code, fromDb);

        fromDb.setFireworkBusinessId(subBusinessId);
        fwOAuthAppMapper.mergeExternalCredentialsToWsDTO(fromDb, response.getBody());
        fwOAuthAppService.updateFwOAuthApp(fromDb);
        businessStoreService.syncBusinessStores();

        return redirectionDataPreparer.preparePluginRedirectionData(of(fromDb));
    }

    @Override
    public RedirectView resetRegistration() {
        var fromDatabase = fwOAuthAppService.getAllFwOAuthApp();

        if (!isEmpty(fromDatabase)) {
            var currentOAuthData = fromDatabase.get(0);
            fwOAuthAppService.deleteFwOAuthApp(currentOAuthData);

            return redirectionDataPreparer.preparePluginRedirectionData(of(FireworkOAuthWsDTO.builder().build()));
        } else {
            return redirectionDataPreparer.preparePluginRedirectionData(of(FireworkOAuthWsDTO.builder().build()));
        }
    }

    @Override
    public RedirectView getRegistrationDataIfPresent() {
        var fromDatabase = fwOAuthAppService.getAllFwOAuthApp();
        return isEmpty(fromDatabase) ? redirectionDataPreparer.preparePluginRedirectionData(of(FireworkOAuthWsDTO.builder().build())) : redirectionDataPreparer.preparePluginRedirectionData(of(fromDatabase.get(0)));
    }

    private RedirectView performOAuthFlowWithOnboarding() {
        var targetToSave = FireworkOAuthWsDTO.builder().build();
        var response = fireworkOAuthClient.performExternalRegistrationRequest();
        var fwOAuthAppWsDTO = fwOAuthAppMapper.mergeExternalOAuthToWsDTO(targetToSave, response.getBody());

        fwOAuthAppService.saveFwOAuthApp(fwOAuthAppWsDTO);

        return redirectionDataPreparer.prepareAuthRedirectionData(fwOAuthAppWsDTO, Boolean.TRUE);
    }

    private RedirectView performOAuthFlowWithoutOnboarding(List<FireworkOAuthWsDTO> fromDatabase) {
        var targetToUpdate = fromDatabase.get(0);
        var response = fireworkOAuthClient.performExternalRegistrationRequest();
        var fwOAuthAppWsDTO = fwOAuthAppMapper.mergeExternalOAuthToWsDTO(targetToUpdate, response.getBody());

        fwOAuthAppService.updateFwOAuthApp(fwOAuthAppWsDTO);

        return redirectionDataPreparer.prepareAuthRedirectionData(fwOAuthAppWsDTO, Boolean.TRUE);
    }

    private RedirectView checkRefreshTokenAndRedirect(FireworkOAuthWsDTO targetToCheck) {
        var refreshToken = targetToCheck.getRefreshToken();
        var expirationDate = targetToCheck.getRefreshTokenExpiresIn();
        var currentDate = new Date();

        if (isPresentSingleExpiredOAuthItem(refreshToken, expirationDate, currentDate)) {
            FireworkOAuthWsDTO data = FireworkOAuthWsDTO.builder()
                    .clientId(targetToCheck.getClientId())
                    .oAuthAppId(targetToCheck.getOAuthAppId())
                    .build();

            return redirectionDataPreparer.prepareAuthRedirectionData(data, Boolean.FALSE);
        }
        if (nonNull(refreshToken)) {
            return redirectionDataPreparer.preparePluginRedirectionData(of(targetToCheck));
        }

        return register();
    }

    private boolean isPresentSingleInvalidOAuthItem(List<FireworkOAuthWsDTO> fromDatabase) {
        return fromDatabase.size() == 1 && isNull(fromDatabase.get(0).getRefreshToken());
    }

    private static boolean isPresentSingleExpiredOAuthItem(String refreshToken, Date expirationDate, Date currentDate) {
        return nonNull(refreshToken) && currentDate.after(expirationDate);
    }
}
