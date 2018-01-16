package cz.melkamar.pyterpreter.parser;

import cz.melkamar.pyterpreter.antlr.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AstPrinter {

    private boolean ignoringWrappers = true;

    public void setIgnoringWrappers(boolean ignoringWrappers) {
        this.ignoringWrappers = ignoringWrappers;
    }

    public void print(ParseTree ctx) {
        explore((RuleContext)ctx, 0);
    }

    private void explore(RuleContext ctx, int indentation) {
        boolean toBeIgnored = ignoringWrappers
                && ctx.getChildCount() == 1
                && ctx.getChild(0) instanceof ParserRuleContext;

        if (ctx instanceof Python3Parser.AtomContext) {
//            System.out.println("atom");

            /*
             * pokud mám node, kterej má 3 childy a levej i pravej má závorky, tak se na něj
             * taky vyseru a pokračuju s dalším.
             */
            if (ctx.getChildCount() == 3 &&
                    ctx.getChild(0) instanceof TerminalNode &&
                    ctx.getChild(2) instanceof TerminalNode &&
                    ((TerminalNode) ctx.getChild(0)).getSymbol().getType() == Python3Parser.OPEN_PAREN &&
                    ((TerminalNode) ctx.getChild(2)).getSymbol().getType() == Python3Parser.CLOSE_PAREN
                    ) toBeIgnored = true;
        }

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentation; i++) {
            indent.append("  ");
        }

        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            if (ruleName.equals("simple_stmt"))
                System.out.print("");

            /*
            * arith_expr - plus, minus
            * term - krát, děleno, atd.
            */

            if (ctx.getRuleIndex() == Python3Parser.RULE_arith_expr || ctx.getRuleIndex() == Python3Parser.RULE_term){}
//                System.out.println(indent + ctx.getChild(1).getText()+"  2");

            else
                System.out.println(indent + "[" + ruleName + "]  3");
        }

        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext) element, indentation + (toBeIgnored ? 0 : 1));
            } else {
                System.out.println(indent + "{" + element.getText()
                        .replace("\n", "\\n")
                        .replace("\r", "\\r") + "}  4");
            }
        }
    }

}
