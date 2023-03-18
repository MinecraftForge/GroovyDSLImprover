/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi

import groovy.transform.CompileStatic
import net.minecraftforge.gdi.util.ConfigurableDSLElement
import net.minecraftforge.gdi.util.ProjectAssociatedDSLElement

/**
 * Defines a DSL object which is configurable and part of a project structure.
 *
 * @param <TSelf> The type of the implementing class.
 */
@CompileStatic
interface BaseDSLElement<TSelf extends BaseDSLElement<TSelf>> extends ConfigurableDSLElement<TSelf>, ProjectAssociatedDSLElement {
}
