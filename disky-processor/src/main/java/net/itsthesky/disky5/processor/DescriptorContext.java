package net.itsthesky.disky5.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

public final class DescriptorContext {
    public final TypeElement descriptor;
    public final String descriptorFqn;
    public final String basePkg;
    public final String generatedPkg;
    public final String jdaType;
    public final String codeName;
    public final String parentDescriptorFqn;
    public final ProcessingEnvironment env;
    public final List<String> generatedClasses;
    public final List<DiSkyProcessor.SyntaxRecord> syntaxRecords;
    public final Set<String> multiTypeNames;
    public final List<DiSkyProcessor.MultiTypeContribution> multiTypeContributions;

    public DescriptorContext(TypeElement descriptor, String jdaType, String codeName,
                             String parentDescriptorFqn, ProcessingEnvironment env,
                             List<String> generatedClasses, List<DiSkyProcessor.SyntaxRecord> syntaxRecords,
                             Set<String> multiTypeNames,
                             List<DiSkyProcessor.MultiTypeContribution> multiTypeContributions) {
        this.descriptor           = descriptor;
        this.descriptorFqn        = descriptor.getQualifiedName().toString();
        this.basePkg              = descriptorFqn.substring(0, descriptorFqn.lastIndexOf('.'));
        this.generatedPkg         = basePkg.replace("descriptors", "generated");
        this.jdaType              = jdaType;
        this.codeName             = codeName;
        this.parentDescriptorFqn  = parentDescriptorFqn;
        this.env                  = env;
        this.generatedClasses     = generatedClasses;
        this.syntaxRecords        = syntaxRecords;
        this.multiTypeNames       = multiTypeNames;
        this.multiTypeContributions = multiTypeContributions;
    }
}
