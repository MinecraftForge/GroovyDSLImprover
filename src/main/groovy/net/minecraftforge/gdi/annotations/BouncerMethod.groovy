package net.minecraftforge.gdi.annotations

import net.minecraftforge.gdi.transformer.BouncerMethodTransformer
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Adds a bouncer synthetic method with the given return type.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@GroovyASTTransformationClass(classes = BouncerMethodTransformer)
@interface BouncerMethod {
    /**
     * The return type of the bouncer method
     */
    Class<?> returnType()
}