package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Exception_init_pragma extends ConfiguredTestFormatter {

    @Test
    public void example_11_5() throws IOException {
        var input = """
                declare
                deadlock_detected
                exception;
                pragma
                exception_init
                (
                deadlock_detected
                ,
                -
                60
                )
                ;
                begin
                null;
                exception
                when deadlock_detected then
                null;
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   deadlock_detected exception;
                   pragma exception_init(deadlock_detected, -60);
                begin
                   null;
                exception
                   when deadlock_detected then
                      null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
