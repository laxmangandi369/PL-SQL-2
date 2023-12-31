package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Deprecate_pragma extends ConfiguredTestFormatter {

    @Test
    public void example_13_12() throws IOException {
        var input = """
                create package pack1 as
                pragma
                deprecate
                (
                pack1
                )
                ;
                procedure foo;
                procedure bar;
                end pack1;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package pack1 as
                   pragma deprecate (pack1);
                   procedure foo;
                   procedure bar;
                end pack1;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_13_13() throws IOException {
        var input = """
                create package pack5 authid definer as
                   pragma
                   deprecate
                   (
                   pack5
                   ,
                   'package pack5 has been deprecated, use new_pack5 instead.'
                   )
                   ;
                   procedure foo;
                   procedure bar;
                end pack5;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package pack5 authid definer as
                   pragma deprecate (
                      pack5,
                      'package pack5 has been deprecated, use new_pack5 instead.'
                   );
                   procedure foo;
                   procedure bar;
                end pack5;
                """;
        assertEquals(expected, actual);
    }
}
