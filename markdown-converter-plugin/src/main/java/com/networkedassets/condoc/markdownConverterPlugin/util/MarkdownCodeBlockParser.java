package com.networkedassets.condoc.markdownConverterPlugin.util;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.ast.VerbatimNode;
import org.pegdown.plugins.InlinePluginParser;

public class MarkdownCodeBlockParser extends Parser implements InlinePluginParser {
    private final static String TAG = "```";

    public MarkdownCodeBlockParser() {
        super(ALL, 1000L, DefaultParseRunnerProvider);
    }

    @Override
    public Rule[] inlinePluginRules() {
        return new Rule[]{
                codeInline()
        };
    }

    public Rule codeInline() {
        StringBuilderVar language = new StringBuilderVar();
        StringBuilderVar code = new StringBuilderVar();

        return NodeSequence(
                Sequence(
                        TAG,
                        OneOrMore(
                                TestNot('\n'),
                                BaseParser.ANY,
                                language.append(matchedChar()
                                )
                        ),
                        push(language.getString().trim())
                ),
                Sequence(
                        OneOrMore(
                                TestNot(TAG),
                                BaseParser.ANY,
                                code.append(matchedChar())
                        ),
                        push(code.getString().trim())
                ),
                TAG,
                push(
                        new VerbatimNode(
                                (String) pop(),
                                (String) pop()
                        )
                )
        );
    }
}