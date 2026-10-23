package net.itsthesky.disky5.processor.handlers;

import net.itsthesky.disky5.processor.DescriptorContext;
import net.itsthesky.disky5.processor.DiSkyProcessor;

public interface DescriptorHandler {
    void handle(DescriptorContext ctx) throws Exception;
}
