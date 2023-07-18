package firework.service.impl;

import com.google.gson.Gson;
import firework.dto.FireworkOAuthWsDTO;
import firework.service.RedirectionDataPreparer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Boolean.TRUE;
import static java.net.URI.create;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
public class RedirectionDataPreparerImpl implements RedirectionDataPreparer {

    @Value("${firework.api.fields.generalName}")
    public String generalName;
    @Value("${firework.api.fields.authorization.client}")
    public String authorizationField;
    @Value("${firework.api.fields.scope}")
    public String scope;
    @Value("${firework.api.fields.responseType}")
    public String responseType;
    @Value("${firework.api.url.extensionUrl}")
    public String extensionUrl;
    @Value("${firework.api.url.backoffice}")
    public String backofficeUrl;
    @Value("${firework.api.fields.grantType}")
    public String grantType;
    @Value("${firework.api.url.oauth.authorize}")
    public String fireworkAuthorizationUrl;
    @Value("${firework.api.url.callbackUrlSuffix}")
    public String callbackUrlSuffix;
    @Value("${firework.api.fields.businessName}")
    public String businessName;
    @Value("${platform.contact.email}")
    private String contact;

    @Override
    public RedirectView prepareAuthRedirectionData(FireworkOAuthWsDTO fwOAuthAppWsDTO, Boolean isBusinessOnboardFlagEnabled) {
        var builder = prepareAuthorizationPayload(fwOAuthAppWsDTO, isBusinessOnboardFlagEnabled);
        var redirectionView = new RedirectView(builder.toUriString());

        redirectionView.setExposeModelAttributes(true);
        redirectionView.addStaticAttribute("redirectionFlag", TRUE);

        return redirectionView;
    }

    @Override
    public RedirectView preparePluginRedirectionData(Optional<FireworkOAuthWsDTO> fwOAuthAppWsDTO) {
        return prepareViewAndData(backofficeUrl, fwOAuthAppWsDTO.get());
    }

    @Override
    public Map<String, Object> prepareDataForTokenRequest(String code, FireworkOAuthWsDTO fromDb) {
        return Map.of(
                "grant_type", grantType,
                "redirect_uri", extensionUrl + callbackUrlSuffix,
                "client_id", fromDb.getClientId(),
                "client_secret", fromDb.getClientSecret(),
                "code", code);
    }

    @Override
    public HttpEntity<String> prepareHttpEntity(Map<String, Object> paramsToPreparationStep) {
        var headers = new HttpHeaders();
        var gson = new Gson();

        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = gson.toJson(paramsToPreparationStep);

        return new HttpEntity<>(json, headers);
    }

    @Override
    public Map<String, Object> prepareRegisterData() {
        return Map.of(
                "client_name", generalName,
                "redirect_uris", List.of(extensionUrl + callbackUrlSuffix),
                "contacts", List.of(contact),
                "scope", scope);
    }

    private UriComponentsBuilder prepareAuthorizationPayload(FireworkOAuthWsDTO fwOAuthAppWsDTO, Boolean businessOnboardingFlag) {
        return fromUri(create(fireworkAuthorizationUrl))
                .queryParam("client", authorizationField)
                .queryParam("response_type", responseType)
                .queryParam("redirect_uri", extensionUrl + callbackUrlSuffix)
                .queryParam("client_id", fwOAuthAppWsDTO.getClientId())
                .queryParam("state", fwOAuthAppWsDTO.getOAuthAppId())
                .queryParam("business_onboard", businessOnboardingFlag)
                .queryParam("business", businessName)
                .queryParam("email", contact)
                .queryParam("website", extensionUrl);
    }

    private RedirectView prepareViewAndData(String uriString, FireworkOAuthWsDTO targetForAttributes) {
        var redirectView = new RedirectView(uriString);
        redirectView.setExposeModelAttributes(true);
        mergeStaticAttributes(redirectView, targetForAttributes);

        return redirectView;
    }

    private void mergeStaticAttributes(RedirectView redirectView, FireworkOAuthWsDTO targetForAttributes) {
        redirectView.addStaticAttribute("clientName", generalName);
        redirectView.addStaticAttribute("businessId", targetForAttributes.getFireworkBusinessId());
    }
}
