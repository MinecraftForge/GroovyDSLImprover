package net.minecraftforge.gdi.annotations

import net.minecraftforge.gdi.transformer.ClosureEquivalentTransformer
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotate a method with an {@link org.gradle.api.Action} parameter with this annotation in order to add
 * a delegate method with the same name and parameters but with a {@link Closure} parameter instead of the action.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@GroovyASTTransformationClass(classes = ClosureEquivalentTransformer)
@interface ClosureEquivalent {

}