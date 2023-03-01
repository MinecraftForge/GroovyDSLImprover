/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi.tests.transforms

import groovy.transform.CompileStatic
import net.minecraftforge.gdi.annotations.ClosureEquivalent
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.gradle.api.Action
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.util.concurrent.atomic.AtomicReference

class ClosureEquivalentTest extends BaseTransformerTesting {
    final ClassNode testedClass = ClassHelper.make(TestInterface)

    @Test
    void "closure equivalent method is generated"() {
        Assertions.assertNotNull(getMethod('invoke', Closure))
    }

    @Test
    void "closure equivalent method delegates"() {
        final AtomicReference<String> ref = new AtomicReference<>()
        new TestInterface() {
            @Override
            void invoke(Action<AtomicReference<String>> action) {
                action.execute(ref)
            }
        }.invokeMethod('invoke', {
            set('hello WoRlD')
        })

        Assertions.assertEquals('hello WoRlD', ref.get())
    }

    @CompileStatic
    static interface TestInterface {
        @ClosureEquivalent
        void invoke(Action<AtomicReference<String>> action)
    }
}
