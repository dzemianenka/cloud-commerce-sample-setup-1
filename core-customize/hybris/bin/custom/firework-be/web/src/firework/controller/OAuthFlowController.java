package firework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import firework.facade.FireworkOAuthFlowFacade;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "OAuth flow controller")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OAuthFlowController {

    private final FireworkOAuthFlowFacade fireworkOAuthFlowFacade;

    @Operation(operationId = "OAuth flow (plugin registration step)", summary = "Endpoint to process data from plugin when registration step")
    @PostMapping("/register")
    public RedirectView register() {
        return fireworkOAuthFlowFacade.register();
    }

    @Operation(operationId = "OAuth flow (data handling step)", summary = "Callback for code state and businessId fields")
    @GetMapping("/oauth_callback")
    public RedirectView callbackToCodeHandling(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "business_id") String subBusinessId) {
        return fireworkOAuthFlowFacade.callbackToCodeHandling(code, state, subBusinessId);
    }
}
