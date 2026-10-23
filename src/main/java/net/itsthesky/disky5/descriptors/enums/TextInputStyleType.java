package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "textinputstyle", jda = TextInputStyle.class)
@Documentation(
    name = "Text Input Style",
    description = "The shape of a modal text input: 'short' is a single line, 'paragraph' a multi-line area.",
    examples = "set {_i} to a new paragraph text input with id \"body\" labeled \"Your message\"",
    since = "5.0.0-a1")
public final class TextInputStyleType {
}
