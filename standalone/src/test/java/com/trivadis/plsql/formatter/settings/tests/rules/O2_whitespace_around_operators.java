package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class O2_whitespace_around_operators extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class True {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundOperators, true);
        }

        @Test
        public void plus_minus_multiply_divide() throws IOException {
            var input = """
                    select a+b-c*d/e from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a + b - c * d / e from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_before() throws IOException {
            var input = """
                    select a1234567890
                           +b1234567890
                           -c1234567890
                           *d1234567890
                           /e1234567890
                      from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a1234567890
                           + b1234567890
                           - c1234567890
                           * d1234567890
                           / e1234567890
                      from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_after() throws IOException {
            var input = """
                    select a1234567890+
                           b1234567890-
                           c1234567890*
                           d1234567890/
                           e1234567890
                      from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a1234567890 +
                           b1234567890 -
                           c1234567890 *
                           d1234567890 /
                           e1234567890
                      from dual;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class False {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundOperators, false);
        }

        @Test
        public void plus_minus_multiply_divide() throws IOException {
            var input = """
                    select a  +  b  -  c  *  d  /  e from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a+b-c*d/e from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_before() throws IOException {
            var input = """
                    select a1234567890
                           + b1234567890
                           - c1234567890
                           * d1234567890
                           / e1234567890
                      from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a1234567890
                           +b1234567890
                           -c1234567890
                           *d1234567890
                           /e1234567890
                      from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_after() throws IOException {
            var input = """
                    select a1234567890 +
                           b1234567890 -
                           c1234567890 *
                           d1234567890 /
                           e1234567890
                      from dual;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a1234567890+
                           b1234567890-
                           c1234567890*
                           d1234567890/
                           e1234567890
                      from dual;
                    """;
            assertEquals(expected, actual);
        }
    }
}
