package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A14_line_break_after_node extends ConfiguredTestFormatter {

    @Test
    public void as_create_view() throws IOException {
        var input = """
                create or replace view v as select * from t;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create or replace view v as
                   select * from t;
                """;
        assertEquals(expected, actual);
    }
}
