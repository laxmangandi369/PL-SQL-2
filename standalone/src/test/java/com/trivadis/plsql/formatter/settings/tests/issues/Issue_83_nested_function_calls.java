package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_83_nested_function_calls extends ConfiguredTestFormatter {

    @Test
    public void select_single_column_with_hint() {
        var sql = """
                select /*+ parallel(t, 2) */
                       a
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void select_two_columns_with_hint() {
        var sql = """
                select /*+ parallel(t, 2) */
                       a,
                       b
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void two_selects_with_hints() {
        var sql = """
                select /*+ parallel(t, 2) */
                       a
                  from t;

                select /*+ parallel(t, 2) */
                       a
                  from t;
                """;
        formatAndAssert(sql);
    }
}
