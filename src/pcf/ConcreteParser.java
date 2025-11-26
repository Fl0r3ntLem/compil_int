package pcf;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.InputStream;

public class ConcreteParser {
    public static ParseTree analyze(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);

        PcfLexer lexer = new ReportingPcfLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PcfParser parser = new PcfParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());

        // generating parse tree
        ParseTree tree = parser.term();
        System.out.println("ANTLR Syntax Tree: " + tree.toStringTree(parser));
        if (ErrorFlag.getFlag()) // exit in case of an error
            throw new SyntaxError(ErrorFlag.getMsg());
        else
            return tree;
    }// else visit parse tree to generate AST   
}
