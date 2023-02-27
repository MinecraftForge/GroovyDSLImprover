/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi.tests.property

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

import java.util.stream.Stream

import static org.junit.jupiter.api.Assertions.*

class PropertyTest {
    @TempDir
    private static File gradleProjectDir
    private static PropertyOwner owner
    private static ClassNode node

    @BeforeAll
    @CompileStatic
    static void setup() {
        final Project project = ProjectBuilder.builder()
                .withProjectDir(gradleProjectDir)
                .withName("DummyTestingProject")
                .build()

        owner = project.getObjects().newInstance(PropertyOwnerImpl.class)
        node = ClassHelper.make(PropertyOwner)
    }

    @Test
    void "String property generates methods"() {
        owner.invokeMethod('stringProperty', 'Some String')
        assertEquals(owner.stringProperty.get(), 'Some String')

        assertNotNull(getMethod('stringProperty', String))
    }

    @Test
    void "boolean 'is' property generates methods"() {
        owner.invokeMethod('existing', true)
        assertEquals(owner.isExisting.get(), true)

        // Methods for wrapper types generate primitive DSL methods
        assertNotNull(getMethod('existing', boolean))
        assertNotNull(getMethod('setExisting', boolean))
    }

    private static MethodNode getMethod(String name, Class... parameters) {
        return node.getMethod(name, Stream.of(parameters).map(ClassHelper.&make).map { GeneralUtils.param(it, 'p') }.toArray(Parameter[]::new))
    }
}
