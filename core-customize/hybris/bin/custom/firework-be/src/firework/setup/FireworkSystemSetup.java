/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package firework.setup;

import de.hybris.platform.core.initialization.SystemSetup;
import firework.constants.FireworkConstants;
import firework.service.FireworkService;

import java.io.InputStream;

import static firework.constants.FireworkConstants.PLATFORM_LOGO_CODE;


@SystemSetup(extension = FireworkConstants.EXTENSIONNAME)
public class FireworkSystemSetup {
    private final FireworkService fireworkService;

    public FireworkSystemSetup(final FireworkService fireworkService) {
        this.fireworkService = fireworkService;
    }

    @SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
    public void createEssentialData() {
        fireworkService.createLogo(PLATFORM_LOGO_CODE);
    }

    private InputStream getImageStream() {
        return FireworkSystemSetup.class.getResourceAsStream("/firework/sap-hybris-platform.png");
    }
}
