package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Procedure_declaration_and_definition extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_procedure_declaration() throws IOException {
        var input = """
                create
                package
                pkg
                is
                procedure
                p
                (
                a in integer
                );
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package pkg 
                is
                   procedure p(
                      a in integer
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_procedure_definition() throws IOException {
        var input = """
                create
                package
                body
                pkg
                is
                procedure
                p
                (
                a in integer
                )
                is
                begin
                null;
                end;
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package body pkg
                is
                   procedure p(
                      a in integer
                   )
                   is
                   begin
                      null;
                   end;
                end;
                """;
        assertEquals(expected, actual);
    }

}
