package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class A4_line_overflow extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class original {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().maxCharLineSize, 120);
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().breaksConcat, Format.Breaks.None);
        }

        @Test
        public void procedure_spec_with_long_line() throws IOException {
            var input = """
                    create or replace package pkg as
                    procedure very_long_line (p_a_12345678901234567890 in integer, p_b_12345678901234567890 in integer, p_c_12345678901234567890 in integer, p_d_12345678901234567890 in integer, p_e_12345678901234567890 in integer, p_f_12345678901234567890 in integer, p_g_12345678901234567890 in integer);
                    procedure short_line (p_a in integer);
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            // a subsequent formatter call will fix the missing indentations
            var expected = """
                    create or replace package pkg as
                       procedure very_long_line(p_a_12345678901234567890 in integer,
                                                p_b_12345678901234567890 in integer,
                                                p_c_12345678901234567890 in integer,
                                                p_d_12345678901234567890 in integer,
                                                p_e_12345678901234567890 in integer,
                                                p_f_12345678901234567890 in integer,
                                                p_g_12345678901234567890 in integer);
                       procedure short_line(p_a in integer);
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_list_with_long_line() throws IOException {
            var input = """
                    select *
                      from t
                     where x in (
                              84,641,8409,8408,647,645,643,9802,9809,9810,9804,9803,9806,9807,75382,75440,75997,76043,76092,76670,76720,76831,77395,77441,77482,78048,78131,78181,78740,78788,78832,79379,79426,79495,80042,80071,80241,80786,80842,80900,81439,81443,81451,81455,81474,81475,81476,81477,81484,81485,81486,81487,81488,81489,81490,81491,81500,81501,81502,81503,81509,81534,81535,81536,81537,81556,81557,81558,81762,81763,81772,82293,82294,82295,82296,82300,82301,82302,82303,82304,82318,82322,82323,82324,82325,82326,82327,82338,82339,82340,82341,82342,82346,82347,82348,82349,82350,82358,82359
                           );
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select *
                      from t
                     where x in (
                              84, 641, 8409, 8408, 647, 645, 643, 9802, 9809, 9810, 9804, 9803, 9806, 9807, 75382, 75440, 75997, 76043, 76092,
                              76670, 76720, 76831, 77395, 77441, 77482, 78048, 78131, 78181, 78740, 78788, 78832, 79379, 79426, 79495, 80042,
                              80071, 80241, 80786, 80842, 80900, 81439, 81443, 81451, 81455, 81474, 81475, 81476, 81477, 81484, 81485, 81486,
                              81487, 81488, 81489, 81490, 81491, 81500, 81501, 81502, 81503, 81509, 81534, 81535, 81536, 81537, 81556, 81557,
                              81558, 81762, 81763, 81772, 82293, 82294, 82295, 82296, 82300, 82301, 82302, 82303, 82304, 82318, 82322, 82323,
                              82324, 82325, 82326, 82327, 82338, 82339, 82340, 82341, 82342, 82346, 82347, 82348, 82349, 82350, 82358, 82359
                           );
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void new_lines_in_strings() {
            var sql = """
                    select 'aaaaaaaaaaa aaaaaaa aaaaa aaaaaaaaaaaaaaaaaaaaaaaa aaaaaa aaaaaa aaaaa
                            bbbbbbbbbbbb bbbbbbbbb bbbbbbb bbbbbbbb bbbb bbb ' || 'ccc' || 'ddd' as value
                      from dual;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void recommendend_way_to_split_list() throws IOException {
            // indent of the first line of the list is the same as for all subsequent list lines
            var input = """
                    select *
                    from table(
                    mdsys.stringlist(
                    'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values'));
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select *
                      from table(
                              mdsys.stringlist(
                                 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some',
                                 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list',
                                 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a',
                                 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this',
                                 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some',
                                 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list',
                                 'with', 'some', 'values'));
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void not_recommendend_way_to_split_list() throws IOException {
            // indent of the first line of the list is different to the indents of the subsequent lines
            var input = """
                    select *
                    from table(
                    mdsys.stringlist('this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values'));
                    """;
            var actual = getFormatter().format(input);
            // the first formatter results produces wrong indents for subsequent lines
            // and A4 might split the lines at the wrong position for subsequent formatter calls
            var expected = """
                    select *
                      from table(
                              mdsys.stringlist('this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list',
                              'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string',
                              'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is',
                              'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values',
                              'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some',
                              'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with',
                              'some', 'values'));
                    """;
            assertEquals(expected, actual);
            // a second formatter call fixes the indent, but adds unwanted line breaks.
            // this is an expected result.
            // fixing that would require a correct indent calculation in A4, which would requires a lot of code duplication.
            var actual2 = getFormatter().format(actual);
            var expected2 = """
                    select *
                      from table(
                              mdsys.stringlist('this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list',
                                 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string',
                                 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is',
                                 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values',
                                 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some',
                                 'values', 'this', 'is', 'a', 'string', 'list', 'with', 'some', 'values', 'this', 'is', 'a', 'string', 'list',
                                 'with',
                                 'some', 'values'));
                    """;
            assertEquals(expected2, actual2);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Commas_before {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().maxCharLineSize, 30);
            setOption(getFormatter().breaksComma, Format.Breaks.Before);
            setOption(getFormatter().breaksConcat, Format.Breaks.Before);
        }

        @Test
        public void insert_select() throws IOException {
            var input = """
                    begin
                       insert into t (
                          column1, column2, column3, column4, column5
                       )
                       select column1, column2, column3, column4, column5
                         from dual;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       insert into t (
                          column1, column2, column3,
                          column4, column5
                       )
                       select column1, column2, column3,
                       column4, column5
                         from dual;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
            var expected2 = """
                    begin
                       insert into t (
                          column1, column2, column3
                        , column4, column5
                       )
                       select column1
                            , column2
                            , column3
                            , column4
                            , column5
                         from dual;
                    end;
                    /
                    """;
            var actual2 = getFormatter().format(actual);
            assertEquals(expected2, actual2);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Commas_after {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().maxCharLineSize, 30);
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().breaksConcat, Format.Breaks.Before);
        }

        @Test
        public void insert_select() throws IOException {
            var input = """
                    begin
                       insert into t (
                          column1, column2, column3, column4, column5
                       )
                       select column1, column2, column3, column4, column5
                         from dual;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       insert into t (
                          column1, column2, column3,
                          column4, column5
                       )
                       select column1, column2, column3,
                       column4, column5
                         from dual;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
            var expected2 = """
                    begin
                       insert into t (
                          column1, column2, column3,
                          column4, column5
                       )
                       select column1,
                              column2,
                              column3,
                              column4,
                              column5
                         from dual;
                    end;
                    /
                    """;
            var actual2 = getFormatter().format(actual);
            assertEquals(expected2, actual2);
        }
    }
}
