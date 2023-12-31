package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class R3_one_command_per_line extends ConfiguredTestFormatter {

    @Nested
    class sqlplus {

        @Test
        public void single_line() throws IOException {
            var input = """
                    column empno format 999; column ename format a20; show con_name
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    column empno format 999;
                    column ename format a20;
                    show con_name
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_multiline() {
            var sql = """
                    column empno format 999
                    column ename format a20

                    show con_name
                    """;
            formatAndAssert(sql);
        }
    }

    @Nested
    class sql {

        @Test
        public void single_line() throws IOException {
            var input = """
                    select * from emp; delete from emp where empno = 9999; update emp set sal=sal+1 where empno=9999;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select * from emp;
                    delete from emp where empno = 9999;
                    update emp set sal = sal + 1 where empno = 9999;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_multiline() {
            var sql = """
                    select * from emp;

                    delete from emp where empno = 9999;
                    update emp set sal = sal + 1 where empno = 9999;
                    """;
            formatAndAssert(sql);
        }

    }

    @Nested
    class plsql {

        @Test
        public void single_line() throws IOException {
            var input = """
                    create procedure p is l1 integer; l2 date; begin null; if l1 = 1 then null; null; elsif l1 = 2 then null; else null; end if; end;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    create procedure p is
                       l1 integer;
                       l2 date;
                    begin
                       null;
                       if l1 = 1 then
                          null;
                          null;
                       elsif l1 = 2 then
                          null;
                       else
                          null;
                       end if;
                    end;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void multiline_fix_indentation() throws IOException {
            var input = """
                    create procedure p is
                           l1 integer;
                           l2 date;
                           begin
                               null;
                               for i in 1..10
                               loop
                                   if l1 = 1 then
                                      null;
                                      null;
                                   elsif l1 = 2 then
                                      null;
                                   else
                                      null;
                                   end if;
                               end loop;
                           end;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    create procedure p is
                       l1 integer;
                       l2 date;
                    begin
                       null;
                       for i in 1..10
                       loop
                          if l1 = 1 then
                             null;
                             null;
                          elsif l1 = 2 then
                             null;
                          else
                             null;
                          end if;
                       end loop;
                    end;
                    """;
            assertEquals(expected, actual);
        }
    }
}
