package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_84_long_assignments extends ConfiguredTestFormatter {

    @Test
    public void assignment_with_long_dotted_expression() {
        var sql = """
                begin
                   l_some_long_named_variable_rec.some_primary_item_id :=
                      p_input_obj.items(i).some_nested_element_array(j).some_value.some_other_value.some_primary_item_id;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void assignment_with_ultra_long_dotted_expression() {
        var sql = """
                begin
                   l_some_long_named_variable_rec.some_primary_item_id :=
                      p_input_obj.items(i).some_nested_element_array(j).some_value.some_other_value.some_primary_item_id1.some_primary_item_id2.
                      some_primary_item_id3.some_primary_item_id4.some_primary_item_id5.some_primary_item_id.some_primary_item_id6.some_primary_item_id7;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
