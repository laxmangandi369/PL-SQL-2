package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Insert_statement_extension extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void insert_record_example_6_58() throws IOException {
        var input = """
                DECLARE
                  default_week  schedule%ROWTYPE;
                  i             NUMBER;
                BEGIN
                  default_week.Mon := '0800-1700';
                  default_week.Tue := '0800-1700';
                  default_week.Wed := '0800-1700';
                  default_week.Thu := '0800-1700';
                  default_week.Fri := '0800-1700';
                  default_week.Sat := 'Day Off';
                  default_week.Sun := 'Day Off';

                  FOR i IN 1..6 LOOP
                    default_week.week    := i;

                    INSERT INTO schedule VALUES default_week;
                  END LOOP;
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   default_week schedule%rowtype;
                   i            number;
                begin
                   default_week.mon := '0800-1700';
                   default_week.tue := '0800-1700';
                   default_week.wed := '0800-1700';
                   default_week.thu := '0800-1700';
                   default_week.fri := '0800-1700';
                   default_week.sat := 'Day Off';
                   default_week.sun := 'Day Off';
                                
                   for i in 1..6
                   loop
                      default_week.week := i;
                                
                      insert into schedule values default_week;
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                begin
                insert
                into
                t
                values
                record;
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   insert into t
                   values
                      record;
                end;
                """;
        assertEquals(expected, actual);
    }
}
