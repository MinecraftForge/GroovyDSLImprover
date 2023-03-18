/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi

import groovy.transform.PackageScope
import org.gradle.util.internal.ConfigureUtil

/**
 * Utility class for configuring objects.
 * This class access gradle internal classes, and as such is not part of the public API.
 */
@PackageScope
class ConfigureUtils {

    /**
     * Configures the given target using the given closure.
     *
     * @param configureClosure The closure to use to configure the target.
     * @param target The target to configure.
     * @return The configured target.
     */
    static <T extends ConfigurableDSLElement<T>> T configureSelf(Closure configureClosure, T target) {
        return ConfigureUtil.configureSelf(configureClosure, target)
    }

    /**
     * Configures the given target using the properties in the given map.
     *
     * @param propertyMap The map containing the properties to configure.
     * @param target The target to configure.
     * @return The configured target.
     */
    static <T extends ConfigurableDSLElement<T>> T configureByMap(Map<String, Object> propertyMap, T target) {
        return ConfigureUtil.configureByMap(propertyMap, target)
    }
}
