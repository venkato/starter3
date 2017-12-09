package net.sf.jremoterun.utilities.groovystrans

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.GStringExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.SourceUnit

import java.util.logging.Logger

//import org.codehaus.groovy.runtime.typehandling.ShortTypeHandlingClassCast
@CompileStatic
class JrrCastTransform extends ClassCodeExpressionTransformer {

    private static final Logger log = Logger.getLogger(JrrCastTransform.name);

    SourceUnit sourceUnit;

    JrrCastTransform(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit
//        log.info "66"
    }

    @Override
    void visitField(FieldNode node) {
//        log.info "${node}"
        Expression expression1 = node.initialExpression
        if (isString(expression1)) {
            ClassNode type = node.type
            boolean enable = false;
            if (enable && visitCastExpression34(type)) {
                log.info("adding constructor for : ${node}")
                ConstructorCallExpression constructorCallExpression = new ConstructorCallExpression(type, expression1);
                node.initialValueExpression = constructorCallExpression
//                node.@initialValueExpression = constructorCallExpression
            }
        }

        super.visitField(node)
    }

//    @Override
//    void visitMethod(MethodNode node) {
//        super.visitMethod(node)
//    }


    @Override
    Expression transform(Expression exp) {
        if (exp instanceof CastExpression) {
            CastExpression ce = (CastExpression) exp;
            boolean b = visitCastExpression33(ce)
            if (b) {
                Expression nestedExpression = ce.getExpression()
                log.info("changing cast in : ${sourceUnit.name}, line : ${ce.lineNumber} , for : ${nestedExpression} . end")
                ConstructorCallExpression constructorCallExpression = new ConstructorCallExpression(ce.type, nestedExpression);

                ce.@expression = constructorCallExpression
                constructorCallExpression.transformExpression(this);
                return constructorCallExpression
            }

        }
        return super.transform(exp)
    }

//    @Override
//    void visitCastExpression(CastExpression expression) {
//        super.visitCastExpression(expression)
//    }

    static HashSet<String> hasStringCOnstructor = new HashSet<>()

    @Override
    void visitExpressionStatement(ExpressionStatement es) {
        Expression expression = es.getExpression()
        if (expression instanceof DeclarationExpression) {
            DeclarationExpression de = (DeclarationExpression) expression;
//            visitExpressionStatementDe(de)
        }
        super.visitExpressionStatement(es)
    }

    void visitExpressionStatementDe(DeclarationExpression de) {
        Expression expression1 = de.rightExpression
        if (isString(expression1)) {
            VariableExpression expression2 = de.getVariableExpression()
            ClassNode type = expression2.type
            if (visitCastExpression34(type)) {
                log.info("adding constructor for : ${de}")
                ConstructorCallExpression constructorCallExpression = new ConstructorCallExpression(type, expression1);
                de.rightExpression = constructorCallExpression
            }
        }

    }

//    @Override
//    void visitDeclarationExpression(DeclarationExpression expression) {
//        log.info "${expression}"
//        super.visitDeclarationExpression(expression)
//    }

    boolean visitCastExpression33(CastExpression expression) {
        ClassNode targetType = expression.getType();
        Expression source = expression.getExpression();
        if (isString(source)) {
            return visitCastExpression34(targetType);
        }
        return false

    }

    boolean visitCastExpression34(ClassNode targetType) {
//        log.info "cp 333 ${expression}"
        String name33 = targetType.name
//            boolean name2 = targetType.hasPackageName()
//        String name1 = targetType.packageName
        if (name33 == 'java.io.File') {
            return false
        }
        if (name33 == GString.getName()) {
            return false
        }
        if (name33.startsWith('java.lang.')) {
            return false
        }
        if (name33.indexOf('.') <= 0) {
            return false;
        }
//        log.info "name33 = ${name33} , package = ${name1}"
        boolean hadStrConstructor = hasStringCOnstructor.contains(name33)
        if (hadStrConstructor) {
            return true;
        }
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(name33)
            clazz.getConstructor(String)
            hasStringCOnstructor.add(name33)
            return true
        } catch (NoSuchMethodException cnfe) {
            log.fine("${cnfe}")
        } catch (ClassNotFoundException cnfe) {
            log.fine("${cnfe}")
        }
        return false;

    }


    boolean isString(Expression source) {
        if (source instanceof ConstantExpression) {
            String text = source.getText()
            if(text=='null'){
                return false
            }
            return true;
        }
        if (source instanceof GStringExpression) {
            return true;
        }
        return false;
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return sourceUnit
    }
}
