package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A5_no_space_before_node extends ConfiguredTestFormatter {

    @Test
    public void concat() throws IOException {
        var input = """
                select 'a'  |   |   'b' from t;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select 'a' || 'b' from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void dot_single_line() throws IOException {
        var input = """
                select t   .   column from t;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select t.column from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void dot_multi_line() throws IOException {
        var input = """
                select t
                       .   column from t;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select t.column from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void plsql_slash() throws IOException {
        var input = """
                begin null; end; /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void expr_slash() throws IOException {
        var input = """
                select 5   /   2
                  from dual;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select 5 / 2
                  from dual;
                """;
        assertEquals(expected, actual);
    }
}
