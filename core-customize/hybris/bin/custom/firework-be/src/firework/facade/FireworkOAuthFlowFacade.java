package firework.facade;

import org.springframework.web.servlet.view.RedirectView;

public interface FireworkOAuthFlowFacade {
    RedirectView register();

    RedirectView resetRegistration();

    RedirectView getRegistrationDataIfPresent();

    RedirectView callbackToCodeHandling(String code, String state, String subBusinessId);
}
