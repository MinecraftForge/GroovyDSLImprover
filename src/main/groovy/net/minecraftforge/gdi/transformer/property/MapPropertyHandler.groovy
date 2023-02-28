/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.gdi.transformer.property

import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Opcodes
import net.minecraftforge.gdi.transformer.DSLPropertyTransformer
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils
import org.gradle.api.Action
import org.gradle.api.provider.MapProperty

@CompileStatic
class MapPropertyHandler implements PropertyHandler, Opcodes {
    private static final ClassNode MAP_PROPERTY_TYPE = ClassHelper.make(MapProperty)

    @Override
    boolean handle(MethodNode methodNode, AnnotationNode annotation, String propertyName, DSLPropertyTransformer.Utils utils) {
        if (!GeneralUtils.isOrImplements(methodNode.returnType, MAP_PROPERTY_TYPE)) return false

        final keyType = methodNode.returnType.genericsTypes[0].type
        final valueType = methodNode.returnType.genericsTypes[1].type
        utils.visitPropertyType(valueType, annotation)
        final singularName = utils.getSingularPropertyName(propertyName, annotation)

        final factoryMethod = utils.factory(valueType, annotation, singularName)
        final valueDelegation = factoryMethod === null ? null : new DSLPropertyTransformer.OverloadDelegationStrategy(1, GeneralUtils.callThisX(factoryMethod.name))

        final actionClazzType = GenericsUtils.makeClassSafeWithGenerics(Action, valueType)

        if (utils.getBoolean(annotation, 'isConfigurable', true)) {
            utils.createAndAddMethod(
                    methodName: singularName,
                    modifiers: ACC_PUBLIC,
                    parameters: [new Parameter(keyType, 'key'), new Parameter(valueType, 'val'), new Parameter(actionClazzType, 'action')],
                    codeExpr: {
                        final valVar = GeneralUtils.localVarX('val', valueType)
                        [
                                GeneralUtils.callX(
                                        GeneralUtils.varX('action', actionClazzType),
                                        'execute',
                                        valVar
                                ),
                                GeneralUtils.callX(GeneralUtils.callThisX(methodNode.name), 'put', GeneralUtils.args(GeneralUtils.localVarX('key', keyType), valVar))
                        ]
                    }(),
                    delegationStrategies: { factoryMethod === null ? [] : [valueDelegation] }
            )

            utils.createAndAddMethod(
                    methodName: singularName,
                    modifiers: ACC_PUBLIC,
                    parameters: [new Parameter(keyType, 'key'), new Parameter(valueType, 'val'), utils.closureParam(valueType)],
                    codeExpr: {
                        final valVar = GeneralUtils.localVarX('val', valueType)
                        utils.delegateAndCall(GeneralUtils.localVarX('closure', DSLPropertyTransformer.RAW_GENERIC_CLOSURE), valVar).tap {
                            it.add(GeneralUtils.callX(GeneralUtils.callThisX(methodNode.name), 'put', GeneralUtils.args(GeneralUtils.localVarX('key', keyType), valVar)))
                        }
                    }(),
                    delegationStrategies: { factoryMethod === null ? [] : [valueDelegation] }
            )
        }

        utils.createAndAddMethod(
                methodName: singularName,
                modifiers: ACC_PUBLIC,
                parameters: [new Parameter(keyType, 'key'), new Parameter(valueType, 'val')],
                codeExpr: [GeneralUtils.callX(GeneralUtils.callThisX(methodNode.name), 'put', GeneralUtils.args(GeneralUtils.localVarX('key', keyType), GeneralUtils.localVarX('val', valueType)))]
        )

        return true
    }
}
