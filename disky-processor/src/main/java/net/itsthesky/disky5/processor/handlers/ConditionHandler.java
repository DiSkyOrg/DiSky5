package net.itsthesky.disky5.processor.handlers;

import net.itsthesky.disky5.annotations.Condition;
import net.itsthesky.disky5.processor.DescriptorContext;
import net.itsthesky.disky5.processor.writers.ConditionWriter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

public final class ConditionHandler implements DescriptorHandler {

    @Override
    public void handle(DescriptorContext ctx) throws Exception {
        for (Element enclosed : ctx.descriptor.getEnclosedElements()) {
            if (enclosed.getKind() != ElementKind.METHOD) continue;
            ExecutableElement method = (ExecutableElement) enclosed;
            Condition cond = method.getAnnotation(Condition.class);
            if (cond == null) continue;

            ConditionWriter.write(ctx, method, cond);
        }
    }
}
