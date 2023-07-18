/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package org.leverx.sapcustombackoffice.widgets;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import firework.facade.FireworkOAuthFlowFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.RedirectView;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.zkoss.zk.ui.Executions.sendRedirect;

@org.springframework.stereotype.Component("sapcustombackofficeController")
public class SapcustombackofficeController extends DefaultWidgetController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FireworkOAuthFlowFacade fireworkOAuthFlowFacade;

    Label clientName;
    Label businessId;

    @Override
    public void initialize(final Component comp) {
        super.initialize(comp);
        doLabelsInitialization();
    }

    @ViewEvent(componentID = "refreshFireworkOAuth", eventName = Events.ON_CLICK)
    public void doRegister() {
        var redirectionData = fireworkOAuthFlowFacade.register();
        extractStaticAttributes(redirectionData);
    }

    @ViewEvent(componentID = "resetFireworkOAuth", eventName = Events.ON_CLICK)
    public void doReset() {
        var redirectionData = fireworkOAuthFlowFacade.resetRegistration();
        extractStaticAttributes(redirectionData);
    }

    private void doLabelsInitialization() {
        var redirectionData = fireworkOAuthFlowFacade.getRegistrationDataIfPresent();
        extractStaticAttributes(redirectionData);
    }

    private void extractStaticAttributes(RedirectView redirectionData) {
        var attributes = redirectionData.getStaticAttributes();

        if (attributes.containsKey("redirectionFlag")) {
            sendRedirect(redirectionData.getUrl());
        } else {
            clientName.setValue(defaultIfNull(attributes.get("clientName"), EMPTY).toString());
            businessId.setValue(defaultIfNull(attributes.get("businessId"), EMPTY).toString());
        }
    }
}
