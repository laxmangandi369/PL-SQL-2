package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_82_outer_apply extends ConfiguredTestFormatter {

    @Test
    public void bulk_collect_outer_apply() {
        var sql = """
                declare
                   l_array my_array_tab;
                begin
                   select t.a,
                          t.b,
                          t.c,
                          n.stuff
                     bulk collect
                     into l_array
                     from some_table s
                    outer apply (s.nested_tab) n;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void bulk_collect_cross_apply() {
        var sql = """
                declare
                   l_array my_array_tab;
                begin
                   select t.a,
                          t.b,
                          t.c,
                          n.stuff
                     bulk collect
                     into l_array
                     from some_table s
                    cross apply (s.nested_tab) n;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
