package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_6_max_one_empty_line extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().kwCase, Format.Case.NoCaseChange);
    }

    @Test
    public void pkg() throws IOException {
        var input = """
                /*
                 * comment before pkg
                 */
                create or replace package pkg is
                   
                     
                    /*
                     * pkg
                     */
                          
                                   
                                                  
                    /*
                     * p1
                     */
                    procedure p1;
                    procedure p2;
                    
                    
                    procedure p3;
                    
                    
                        -- p4 (1)
                        
                     
                        
                      -- p4 (2)
                    
                    
                    
                    procedure p4;
                    
                    
                    -- p5
                    procedure p5;
                    
                end pkg;
                /
                """;
        var expected = """
                /*
                 * comment before pkg
                 */
                create or replace package pkg is

                   /*
                    * pkg
                    */

                   /*
                    * p1
                    */
                   procedure p1;
                   procedure p2;

                   procedure p3;

                   -- p4 (1)

                   -- p4 (2)

                   procedure p4;

                   -- p5
                   procedure p5;

                end pkg;
                /
                """.trim();
        var actual = getFormatter().format(input);
        Assertions.assertEquals(expected, actual);
    }
}
