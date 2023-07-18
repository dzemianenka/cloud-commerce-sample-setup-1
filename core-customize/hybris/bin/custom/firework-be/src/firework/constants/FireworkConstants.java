/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package firework.constants;

/**
 * Global class for all Firework constants. You can add global constants for your extension into this class.
 */
public final class FireworkConstants extends GeneratedFireworkConstants {
    public static final String EXTENSIONNAME = "firework";
    public static final String GET_ALL_FWOAUTH_QUERY = "SELECT {pk} FROM {FWOAUTHAPP}";
    public static final String GET_BY_APP_ID_QUERY = "SELECT {pk} FROM {FWOAUTHAPP as fwoaapp} WHERE {fwoaapp.oAuthAppId}=?oAuthAppId";
    public static final String GET_ALL_FWAPI_QUERY = "SELECT {pk} FROM {FwApiCredentials}";
    public static final String GET_BY_STORE_ID_QUERY = "SELECT {pk} FROM {FwApiCredentials as fwacr} WHERE {fwacr.fireworkstoreid}=?fireworkStoreId";
    public static final String PLATFORM_LOGO_CODE = "fireworkPlatformLogo";

    // implement here constants used by this extension

    private FireworkConstants() {
        //empty to avoid instantiating this constant class
    }
}
