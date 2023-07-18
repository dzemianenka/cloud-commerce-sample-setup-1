/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package firework.service;

public interface FireworkService {
    String getHybrisLogoUrl(String logoCode);

    void createLogo(String logoCode);
}
