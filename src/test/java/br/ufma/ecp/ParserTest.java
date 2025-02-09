package br.ufma.ecp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class ParserTest extends TestSupport {

    @Test
    public void testParseTermInteger () {
    var input = "10;";
    var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
    parser.parseTerm();
    var expectedResult =  """
        <term>
        <integerConstant> 10 </integerConstant>
        </term>
        """;
            
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);    

    }


    @Test
    public void testParseTermIdentifer() {
        var input = "varName;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();
    
        var expectedResult =  """
        <term>
        <identifier> varName </identifier>
        </term>
        """;
            
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);    

    }



    @Test
    public void testParseTermString() {
        var input = "\"Hello World\"";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();
    
        var expectedResult =  """
        <term>
        <stringConst> Hello World </stringConst>
        </term>
        """;
            
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);    

    }

    @Test
    public void testParseExpressionSimple() {
        var input = "10+20";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseExpression();
        
        var expectedResult =  """
        <expression>
        <term>
        <integerConstant> 10 </integerConstant>
        </term>
        <symbol> + </symbol>
        <term>
        <integerConstant> 20 </integerConstant>
        </term>
        </expression>
        """;
            
        var result = parser.XMLOutput();
        result = result.replaceAll("\r", ""); 
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);    

    }

    @Test
    public void testParseLetSimple() {
        var input = "let var1 = 10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
				var expectedResult =  """
        <letStatement>
        <keyword> let </keyword>
        <identifier> var1 </identifier>
        <symbol> = </symbol>
        <expression>
        <term>
        <integerConstant> 10 </integerConstant>
        </term>
        <symbol> + </symbol>
        <term>
        <integerConstant> 20 </integerConstant>
        </term>
        </expression>
        <symbol> ; </symbol>
    </letStatement> 
				""";
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLetSimple1() {
        var input = "let var1[1] = 10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
				var expectedResult =  """
        <letStatement>
        <keyword> let </keyword>
        <identifier> var1 </identifier>
        <symbol> [ </symbol>
            <expression>
            <term>
            <integerConstant> 1 </integerConstant>
            </term>
            </expression>
        <symbol> ] </symbol>
        <symbol> = </symbol>
        <expression>
        <term>
        <integerConstant> 10 </integerConstant>
        </term>
        <symbol> + </symbol>
        <term>
        <integerConstant> 20 </integerConstant>
        </term>
        </expression>
        <symbol> ; </symbol>
    </letStatement> 
				""";
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    // @Test
    // public void testParseSubroutineCall() {
    //     var input = "hello()";
    //     var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
    //     parser.parseSubroutineCall();
        
    //     var expectedResult =  """
    //     <identifier> hello </identifier>
    //     <symbol> ( </symbol>
    //     <symbol> ) </symbol>
    //     """;
    //     var result = parser.XMLOutput();
    //     result = result.replaceAll("\r", "");
    //     expectedResult = expectedResult.replaceAll("  ", "");
    //     assertEquals(expectedResult, result);
    // }

    @Test
    public void testParseDo() {
        var input = "do Sys.wait(5);";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseDo();

        var expectedResult = """
            <doStatement>
            <keyword> do </keyword>
            <identifier> Sys </identifier>
            <symbol> . </symbol>
            <identifier> wait </identifier>
            <symbol> ( </symbol>
            <expressionList>
            <expression>
                <term>
                <integerConstant> 5 </integerConstant>
                </term>
            </expression>
            </expressionList>
            <symbol> ) </symbol>
            <symbol> ; </symbol>
            </doStatement>
                """;
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParserWithLessSquareGame() throws IOException {
        var input = fromFile("ExpressionLessSquare/SquareGame.jack");
        var expectedResult =  fromFile("ExpressionLessSquare/SquareGame.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParserWithSquareGame() throws IOException {
        var input = fromFile("Square/SquareGame.jack");
        var expectedResult =  fromFile("Square/SquareGame.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }


    @Test
    public void testParserWithSquare() throws IOException {
        var input = fromFile("Square/Square.jack");
        var expectedResult =  fromFile("Square/Square.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }

}