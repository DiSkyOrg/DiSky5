package net.itsthesky.disky5.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.Compiler.javac;

final class ProcessorTestSupport {

    private ProcessorTestSupport() {
    }

    static JavaFileObject descriptor(String simpleName, String... body) {
        StringBuilder sb = new StringBuilder();
        sb.append("package d.descriptors;\n")
            .append("import net.itsthesky.disky5.annotations.*;\n")
            .append("import net.itsthesky.disky5.annotations.docs.*;\n")
            .append("import net.itsthesky.disky5.annotations.events.*;\n")
            .append("import net.dv8tion.jda.api.entities.*;\n")
            .append("import net.dv8tion.jda.api.events.message.MessageReceivedEvent;\n")
            .append("import net.dv8tion.jda.api.requests.RestAction;\n")
            .append("import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;\n")
            .append("import net.dv8tion.jda.api.requests.restaction.RoleAction;\n")
            .append("import java.util.List;\n");
        for (String line : body)
            sb.append(line).append('\n');
        return JavaFileObjects.forSourceString("d.descriptors." + simpleName, sb.toString());
    }

    static Compilation compile(JavaFileObject... sources) {
        return compiler().compile(sources);
    }

    static Compiler compiler() {
        return javac().withProcessors(new DiSkyProcessor());
    }
}
