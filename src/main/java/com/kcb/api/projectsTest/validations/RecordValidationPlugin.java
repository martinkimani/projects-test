package com.kcb.api.projectsTest.validations;

import static net.bytebuddy.matcher.ElementMatchers.annotationType;
import static net.bytebuddy.matcher.ElementMatchers.hasAnnotation;

import java.io.IOException;

import jakarta.validation.Constraint;

import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;


/**
 *
 * @author martin
 */
public class RecordValidationPlugin implements Plugin{

    @Override
    public boolean matches(TypeDescription target) {
        return target.isRecord() && target.getDeclaredMethods()
                .stream()
                .anyMatch(m -> m.isConstructor() && isConstrained(m));
    }

    @Override
    public Builder<?> apply(Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        return builder.constructor(this::isConstrained)
                .intercept(SuperMethodCall.INSTANCE.andThen(
                        MethodDelegation.to(RecordValidationInterceptor.class)));
    }

    private boolean isConstrained(MethodDescription method) {
        return hasConstrainedReturnValue(method) || hasConstrainedParameter(method);
    }

    private boolean hasConstrainedReturnValue(MethodDescription method) {
        return !method.getDeclaredAnnotations()
                .asTypeList()
                .filter(hasAnnotation(annotationType(Constraint.class)))
                .isEmpty();
    }

    private boolean hasConstrainedParameter(MethodDescription method) {
        return method.getParameters()
                .asDefined()
                .stream()
                .anyMatch(p -> isConstrained(p));
    }

    private boolean isConstrained(ParameterDescription.InDefinedShape parameter) {
        return !parameter.getDeclaredAnnotations()
                .asTypeList()
                .filter(hasAnnotation(annotationType(Constraint.class)))
                .isEmpty();
    }

    @Override
    public void close() throws IOException {
    }
}