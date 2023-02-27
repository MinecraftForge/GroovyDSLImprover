package net.minecraftforge.gdi.transformer

import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.sc.StaticCompileTransformation
import org.gradle.api.Action

import java.util.function.Consumer
import java.util.stream.Stream

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ClosureEquivalentTransformer extends AbstractASTTransformation implements Opcodes {
    private static final ClassNode ACTION = ClassHelper.make(Action)
    private static final ClassNode CLOSURE_TO_ACTION = ClassHelper.make(ClosureToAction)
    private static final ClassNode CSTATIC = ClassHelper.make(CompileStatic)
    @Override
    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        this.init(astNodes, sourceUnit)
        if (astNodes[1] !instanceof MethodNode) return
        final method = (MethodNode) astNodes[1]
        ClassNode clazz = method.declaringClass
        if (clazz instanceof InnerClassNode && clazz.name.endsWith('Trait$Helper')) {
            clazz = ((InnerClassNode) clazz).outerClass
        }

        final actionParam = method.parameters.find { it.type == ACTION }
        final closureParam = DSLPropertyTransformer.Utils.closureParam(actionParam.type.genericsTypes[0].type)
        final stmt = GeneralUtils.callX(VariableExpression.THIS_EXPRESSION, method.name, GeneralUtils.args(
                Stream.of(method.parameters).filter { it.name != '$self' }.<Expression>map {
                    it === actionParam ? asAction(GeneralUtils.varX(closureParam)) : GeneralUtils.varX(it)
                }.toList()
        ))
        final mtd = clazz.addMethod(
                method.name, ACC_PUBLIC,
                method.returnType, Stream.of(method.parameters).filter { it.name != '$self'}.map { it === actionParam ? closureParam : it }.<Parameter>toArray(Parameter[]::new),
                method.exceptions,
                method.returnType == ClassHelper.VOID_TYPE ? GeneralUtils.stmt(stmt) : GeneralUtils.returnS(stmt)
        )
        mtd.setGenericsTypes(method.genericsTypes)
        final ann = new AnnotationNode(CSTATIC)
        mtd.addAnnotation(ann)
        new StaticCompileTransformation().visit(new ASTNode[] {
                ann, mtd
        }, sourceUnit)
    }

    static Expression asAction(Expression closure) {
//        final it = GeneralUtils.param(ClassHelper.OBJECT_TYPE, 'it')
//        final clos = GeneralUtils.closureX(new Parameter[] {it}, GeneralUtils.block(
//                GeneralUtils.stmt(GeneralUtils.callX(closure, 'setDelegate', GeneralUtils.varX(it))),
//                GeneralUtils.stmt(GeneralUtils.callX(closure, 'setResolveStrategy', GeneralUtils.constX(Closure.DELEGATE_FIRST))),
//                GeneralUtils.stmt(GeneralUtils.callX(closure, 'call', GeneralUtils.varX(it)))
//        ))
//        expr.accept(clos)
//        GeneralUtils.castX(ACTION, clos)
        GeneralUtils.callX(CLOSURE_TO_ACTION, 'delegateAndCall', closure)
    }
}
