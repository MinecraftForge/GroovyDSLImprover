/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi

import groovy.transform.CompileStatic
import net.minecraftforge.gdi.annotations.DSLProperty
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty

/**
 * Represents part of an extension which combines a set of files with entries as well as raw addable entries.
 *
 * @param <TSelf> The type of the implementing class.
 */
@CompileStatic
interface BaseDSLElementWithFilesAndEntries<TSelf extends BaseDSLElementWithFilesAndEntries<TSelf>> extends BaseDSLElement<TSelf> {

    /**
     * @return The files which contain entries relevant to this extension.
     */
    @DSLProperty
    ConfigurableFileCollection getFiles()

    /**
     * @return The raw additional entries relevant to this extension.
     */
    @DSLProperty(propertyName = "entry")
    ListProperty<String> getEntries()

    /**
     * Indicates if either at least one file is specified or at least one additional raw entry is specified.
     *
     * @return {@code true}, when at least one file or entry is specified, {@code false} otherwise.
     */
    boolean isEmpty()
}
