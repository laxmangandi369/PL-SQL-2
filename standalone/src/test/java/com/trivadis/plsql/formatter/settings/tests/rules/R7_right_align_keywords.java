package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class R7_right_align_keywords extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Select_commas_before {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.Before);
            setOption(getFormatter().spaceAfterCommas, true);
            setOption(getFormatter().breakOnSubqueries, false);
        }

        @Test
        public void select_into_statement() throws IOException {
            var input = """
                    begin
                    select -- comment
                    all count(*)
                    into x
                    from t1
                    join t2 on t1.c1 = t2.c1
                    left join t3 on t3.c2 = t2.c2
                    cross join t4
                    where t1.c5 = t3.c5
                    and t4.c1 = 'hello'
                    group by t1.c7
                    having count(*) > 0
                    order by 1;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       select -- comment
                          all count(*)
                         into x
                         from t1
                         join t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_inline() throws IOException {
            var input = """
                    begin
                    select -- a comment
                    all count(*)
                    into x
                    from t1
                    join (select c1, c2, c3, c4
                    from t2) t2
                    on t1.c1 = t2.c1
                    left join t3 on t3.c2 = t2.c2
                    cross join t4
                    where t1.c5 = t3.c5
                    and t4.c1 = 'hello'
                    group by t1.c7
                    having count(*) > 0
                    order by 1;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       select -- a comment
                          all count(*)
                         into x
                         from t1
                         join (select c1, c2, c3, c4
                                 from t2) t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_keep_newline() {
            var sql = """
                    begin
                       select -- a comment
                          all count(*)
                         into x
                         from t1
                         join (
                                 select c1, c2, c3, c4
                                   from t2
                              ) t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void select_with_column_list() throws IOException {
            var input = """
                    create view v as
                    select a
                    ,b
                    ,c
                    from t;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    create view v as
                       select a
                            , b
                            , c
                         from t;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Select_commas_after {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_with_column_list() throws IOException {
            var input = """
                    create view v as
                    select a,
                    b,
                    c
                    from t;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    create view v as
                       select a,
                              b,
                              c
                         from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and b = 3 or c = 4;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and b = 3
                        or c = 4;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_case_in_boolean() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where case when a = 2 and b = 3 or c = 4 then 1 end = 1;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where case
                              when a = 2
                                 and b = 3
                                 or c = 4
                              then
                                 1
                           end = 1;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean_in_parenthesis() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and (b = 3 or c = 4);
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and (b = 3 or c = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean_in_parenthesis_and_line_break() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and (b = 3
                    or c = 4);
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and (b = 3
                              or c = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_distinct_with_enough_space() throws IOException {
            var input = """
                    begin
                    for r in (
                    select -- force line break
                    distinct deptno
                    from emp
                    )
                    loop
                    null;
                    end loop;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       for r in (
                          select -- force line break
                        distinct deptno
                            from emp
                       )
                       loop
                          null;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_distinct_with_insufficient_space() throws IOException {
            var input = """
                    select -- force line break
                    distinct deptno
                    from emp;
                    """;
            var actual = getFormatter().format(input);
            // do move right-margin!
            var expected = """
                    select -- force line break
                    distinct deptno
                      from emp;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Insert {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void single_table() throws IOException {
            var input = """
                    begin
                    insert
                    into t
                    values ('a', 'b');
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       insert into t
                       values ('a', 'b');
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void single_table_column_list() throws IOException {
            var input = """
                    begin
                    insert
                    into mytable t (
                    a, b, c, d
                    )
                    values (
                    'a',
                    'b',
                    'c',
                    'd'
                    )
                    returning a,
                    b,
                    c,
                    d
                    into l_a,
                    l_b,
                    l_c,
                    l_d
                    log errors into mytable_errors ('bad') reject limit 10;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       insert into mytable t (
                          a, b, c, d
                       )
                       values (
                          'a',
                          'b',
                          'c',
                          'd'
                       )
                    returning a,
                              b,
                              c,
                              d
                         into l_a,
                              l_b,
                              l_c,
                              l_d
                          log errors into mytable_errors ('bad') reject limit 10;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void multi_table() throws IOException {
            var input = """
                    insert
                    all
                    into t1 (c1, c2)
                    values (c1, c2)
                    into t2 (c1, c2)
                    values (c1, c2)
                    into t3 (c1, c2)
                    values (c1, c2)
                    select c1, c2
                    from t4;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    insert all
                      into t1 (c1, c2)
                    values (c1, c2)
                      into t2 (c1, c2)
                    values (c1, c2)
                      into t3 (c1, c2)
                    values (c1, c2)
                    select c1, c2
                      from t4;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void subselect() throws IOException {
            var input = """
                    begin
                    insert
                    into phs1 (c1)
                              (
                                 select 1 as c1
                    from dual
                              );
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       insert into phs1 (c1)
                       (
                          select 1 as c1
                            from dual
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Update {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void update_table() throws IOException {
            var input = """
                    update employees
                    set job_id = 'SA_MAN'
                    ,salary = salary + 1000
                    ,department_id = 120
                    where first_name = 'Douglas'
                    and last_name = 'Grant';
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    update employees
                       set job_id = 'SA_MAN',
                           salary = salary + 1000,
                           department_id = 120
                     where first_name = 'Douglas'
                       and last_name = 'Grant';
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void update_table_return() throws IOException {
            var input = """
                    begin
                    update t
                    set c1 = 1,
                    c2 = 2,
                    c3 = 3
                    where 1=1
                    return c1, c2
                    into l1, l2
                    log errors into error_table
                    reject limit 10;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       update t
                          set c1 = 1,
                              c2 = 2,
                              c3 = 3
                        where 1 = 1
                       return c1, c2
                         into l1, l2
                          log errors into error_table
                       reject limit 10;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Delete {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void delete_from_where() throws IOException {
            var input = """
                    delete
                    from
                    t
                    where c1 = 1
                    and c2 = 2;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    delete
                      from t
                     where c1 = 1
                       and c2 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void delete_without_from_return_into_log_errors() throws IOException {
            var input = """
                    begin
                    delete
                    t
                    where 1 = 1
                    return c1, c2
                    into l1, l2
                    log errors into error_table
                    reject limit 10;
                    end;
                    /
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    begin
                       delete t
                        where 1 = 1
                       return c1, c2
                         into l1, l2
                          log errors into error_table
                       reject limit 10;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Merge {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void merge_update() throws IOException {
            var input = """
                    merge into t
                    using s
                    on (
                    s.id = t.id
                    and s.id2 = t.id2
                    )
                    when matched then
                    update
                    set t.c1 = s.c1
                    ,t.c2 = s.c2
                    where 1 = 1
                    and 2 = 2;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    merge into t
                    using s
                       on (
                             s.id = t.id
                             and s.id2 = t.id2
                          )
                     when matched then
                          update
                             set t.c1 = s.c1,
                                 t.c2 = s.c2
                           where 1 = 1
                             and 2 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void merge_update_delete() throws IOException {
            var input = """
                    merge into t
                    using s on (s.id = t.id)
                    when matched then
                    update
                    set t.c1 = s.c1,
                    t.c2 = s.c2
                    where 1 = 1
                    and 2 = 2
                    delete
                    where 1 = 2
                    and 2 = 1;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    merge into t
                    using s
                       on (s.id = t.id)
                     when matched then
                          update
                             set t.c1 = s.c1,
                                 t.c2 = s.c2
                           where 1 = 1
                             and 2 = 2
                          delete
                           where 1 = 2
                             and 2 = 1;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void merge_update_delete_insert() throws IOException {
            var input = """
                    merge into t
                    using s on (s.id = t.id)
                    when matched then
                    update
                    set t.c1 = s.c1,
                    t.c2 = s.c2
                    where 1 = 1
                    and 2 = 2
                    delete
                    where 1 = 2
                    and 2 = 1
                    when not matched then
                    insert (
                    t.id,
                    t.c1,
                    t.c2
                    )
                    values (
                    s.id,
                    s.c1,
                    s.c2
                    )
                    where s.c3 = 3;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    merge into t
                    using s
                       on (s.id = t.id)
                     when matched then
                          update
                             set t.c1 = s.c1,
                                 t.c2 = s.c2
                           where 1 = 1
                             and 2 = 2
                          delete
                           where 1 = 2
                             and 2 = 1
                     when not matched then
                          insert (
                             t.id,
                             t.c1,
                             t.c2
                          )
                          values (
                             s.id,
                             s.c1,
                             s.c2
                          )
                           where s.c3 = 3;
                    """;
            assertEquals(expected, actual);
        }
    }
}
