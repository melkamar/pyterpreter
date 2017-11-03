package cz.melkamar.pyterpreter;

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

    public void print(RuleContext ctx) {
        explore(ctx, 0);
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
        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            if (ruleName.equals("integer"))
                System.out.print("");
            for (int i = 0; i < indentation; i++) {
                System.out.print("  ");
            }

            /*
            * arith_expr - plus, minus
            * term - krát, děleno, atd.
            */

            if (ctx.getRuleIndex() == Python3Parser.RULE_arith_expr
                    || ctx.getRuleIndex() == Python3Parser.RULE_term)
                System.out.println(ctx.getChild(1).getText());
            else
                System.out.println(ruleName);
        }
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext) element, indentation + (toBeIgnored ? 0 : 1));
            }
        }
    }

}
