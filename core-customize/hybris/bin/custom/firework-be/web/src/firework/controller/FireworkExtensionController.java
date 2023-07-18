/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package firework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Firework Extension Controller")
@Controller
public class FireworkExtensionController {
    @Operation(operationId = "printAllOAuthFlowParams", summary = "Print All OAuth Flow Params")
    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public String oauthFlowWithParams(final ModelMap model,
                                      @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                      @RequestParam(value = "clientName", defaultValue = "") String clientName,
                                      @RequestParam(value = "id", defaultValue = "") String id,
                                      @RequestParam(value = "clientSecret", defaultValue = "") String clientSecret,
                                      @RequestParam(value = "businessId", defaultValue = "") String businessId,
                                      @RequestParam(value = "businessName", defaultValue = "") String businessName,
                                      @RequestParam(value = "accessToken", defaultValue = "") String accessToken,
                                      @RequestParam(value = "refreshToken", defaultValue = "") String refreshToken) {

        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", clientName);
        model.addAttribute("id", id);
        model.addAttribute("clientSecret", clientSecret);
        model.addAttribute("businessId", businessId);
        model.addAttribute("businessName", businessName);
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);

        return "welcome";
    }
}
