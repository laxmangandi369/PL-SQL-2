package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class For_loop_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized() throws IOException {
        var input = """
                begin
                <
                <
                example
                >
                >
                for
                i
                in
                reverse
                1
                .
                .
                10
                loop
                null
                ;
                end
                loop
                example;
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   <<example>>
                   for i in reverse 1..10
                   loop
                      null;
                   end loop example;
                end;
                """;
        assertEquals(expected, actual);
    }
}
