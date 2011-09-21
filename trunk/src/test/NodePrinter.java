////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2004-2007 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

/*
 * Written by Jeff Dyer
 * Copyright (c) 1998-2003 Mountain View Compiler Company
 * All rights reserved.
 */

package test;

import static macromedia.asc.parser.Tokens.CONST_TOKEN;
import static macromedia.asc.parser.Tokens.DOT_TOKEN;
import static macromedia.asc.parser.Tokens.DOUBLEDOT_TOKEN;
import static macromedia.asc.parser.Tokens.EMPTY_TOKEN;
import static macromedia.asc.parser.Tokens.GET_TOKEN;
import static macromedia.asc.parser.Tokens.LEFTBRACKET_TOKEN;
import static macromedia.asc.parser.Tokens.LEFTPAREN_TOKEN;
import static macromedia.asc.parser.Tokens.SET_TOKEN;

import macromedia.asc.parser.*;
import macromedia.asc.semantics.Value;
import macromedia.asc.util.Context;
import macromedia.asc.util.NumberUsage;

/**
 * NodePrinter.h
 *
 * This visitor prints the parse tree
 *
 * @author Jeff Dyer
 */
public class NodePrinter implements Evaluator
{
    private WriteDestination out;
    private int level;
    private int mode;

    private void separate()
    {
    }

    private void push_in()
    {
        ++level;
        out.write(" ");
    }

    private void pop_out()
    {
        --level;
    }

    private void indent()
    {
        if (mode == man_mode)
        {
            out.writeln();
            for (int i = level; i != 0; --i)
            {
                out.write("  ");
            }
        }
    }

// public:

    public static final int man_mode = 0;
    public static final int machine_mode = 1;

    public boolean checkFeature(Context cx, Node node)
    {
        return true; // return true;
    }

    public NodePrinter(WriteDestination out)
    {
        this.out = out;
        this.level = 0;
    }

    // Base node

    public Value evaluate(Context cx, Node node)
    {
        indent();
        out.write("error:undefined printer method");
        return null;
    }

    // Expression evaluators

    public Value evaluate(Context cx, IdentifierNode node)
    {
        indent();

        if(node instanceof TypeIdentifierNode)
        {
            out.write("typeidentifier ");
        }
        else if (node.isAttr())
        {
            out.write("attributeidentifier ");
        }
        else
        {
            out.write("identifier ");
        }
        out.write(node.name);
        if(node instanceof TypeIdentifierNode)
        {
            out.write("types ");
            push_in();
            ((TypeIdentifierNode)node).typeArgs.evaluate(cx, this);
            pop_out();
        }
        return null;
    }

    // Expression evaluators

    public Value evaluate(Context cx, IncrementNode node)
    {
        indent();
        out.write("increment");
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == DOT_TOKEN ? " dot" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " error"));
        out.write((node.isPostfix ? " postfix " : " prefix ") + Token.getTokenClassName(node.op));
        push_in();
        pop_out();
        separate();
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ThisExpressionNode node)
    {
        indent();
        out.write("this");
        return null;
    }

    public Value evaluate(Context cx, QualifiedIdentifierNode node)
    {
        indent();
        if (node.isAttr())
        {
            out.write("qualifiedattributeidentifier ");
        }
        else
        {
            out.write("qualifiedidentifier ");
        }
        out.write(node.name);
        if (node.qualifier != null)
        {
            push_in();
            indent();
            out.write("qualifier");
            push_in();
            node.qualifier.evaluate(cx, this);
            pop_out();
            pop_out();
        }
        return null;
    }

    public Value evaluate(Context cx, QualifiedExpressionNode node)
    {
        indent();
        if (node.isAttr())
        {
            out.write("qualifiedattributeexpression ");
        }
        else
        {
            out.write("qualifiedexpression ");
        }
        out.write(node.name);
        if (node.qualifier != null)
        {
            push_in();
            indent();
            out.write("qualifier");
            push_in();
            node.qualifier.evaluate(cx, this);
            pop_out();
            pop_out();
        }
        if (node.expr != null)
        {
            push_in();
            indent();
            out.write("expr");
            push_in();
            node.expr.evaluate(cx, this);
            pop_out();
            pop_out();
        }
        return null;
    }

    public Value evaluate(Context cx, LiteralBooleanNode node)
    {
        indent();
        out.write("literalboolean ");
        out.write(node.value ? 1 : 0);
        return null;
    }

    public Value evaluate(Context cx, LiteralNumberNode node)
    {
        indent();
        out.write("literalnumber:");
        out.write(node.value);
        return null;
    }

    public Value evaluate(Context cx, LiteralStringNode node)
    {
        indent();
        out.write("literalstring:");
        out.write(node.value);
        return null;
    }

    public Value evaluate(Context cx, LiteralNullNode node)
    {
        indent();
        out.write("literalnull");
        return null;
    }

    public Value evaluate(Context cx, LiteralRegExpNode node)
    {
        indent();
        out.write("literalregexp:");
        out.write(node.value);
        return null;
    }

    public Value evaluate(Context cx, LiteralXMLNode node)
    {
        indent();
        out.write("literalxml");
        push_in();
        if (node.list != null)
        {
            node.list.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, FunctionCommonNode node)
    {
        indent();
        out.write("functioncommon");
        push_in();
        if (node.signature != null)
        {
            node.signature.evaluate(cx, this);
        }
        separate();
        if (node.body != null)
        {
            node.body.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ParenExpressionNode node)
    {
        indent();
        out.write("paren");
        return null;
    }

    public Value evaluate(Context cx, ParenListExpressionNode node)
    {
        indent();
        out.write("parenlist");
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        return null;
    }

    public Value evaluate(Context cx, LiteralObjectNode node)
    {
        indent();
        out.write("literalobject");
        push_in();
        if (node.fieldlist != null)
        {
            node.fieldlist.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, LiteralFieldNode node)
    {
        indent();
        out.write("literalfield");
        push_in();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        separate();
        if (node.value != null)
        {
            node.value.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, LiteralArrayNode node)
    {
        indent();
        out.write("literalarray");
        push_in();
        if (node.elementlist != null)
        {
            node.elementlist.evaluate(cx, this);
        }
        pop_out();
        return null;
    }
    
    public Value evaluate(Context cx, LiteralVectorNode node)
    {
        indent();
        out.write("new<");
        node.type.evaluate(cx, this);
        out.write(">");
        
        push_in();
        if (node.elementlist != null)
        {
            node.elementlist.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, SuperExpressionNode node)
    {
        indent();
        out.write("superexpression");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, MemberExpressionNode node)
    {
        indent();
        out.write("member");
        push_in();
        if (node.base != null)
        {
            node.base.evaluate(cx, this);
        }
        separate();
        if (node.selector != null)
        {
            node.selector.evaluate(cx, this);
        }
        pop_out();

        return null;
    }

    public Value evaluate(Context cx, InvokeNode node)
    {
        indent();
        out.write("invoke");
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " dot"));
        push_in();
        out.write(node.name);
        separate();
        if (node.args != null)
        {
            node.args.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, CallExpressionNode node)
    {
        indent();
        out.write((node.is_new ? "construct" : "call"));
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " dot"));
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        separate();

        if (node.args != null)
        {
            node.args.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, DeleteExpressionNode node)
    {
        indent();
        out.write("delete");
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " dot"));
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ApplyTypeExprNode node)
    {
        indent();
        out.write("applytype");
        push_in();
        node.typeArgs.evaluate(cx, this);
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, GetExpressionNode node)
    {
        indent();
        out.write("get");
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " dot"));
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, SetExpressionNode node)
    {
        indent();
        out.write("set");
        out.write((node.getMode() == LEFTBRACKET_TOKEN ? " bracket" :
            node.getMode() == LEFTPAREN_TOKEN ? " filter" :
            node.getMode() == DOUBLEDOT_TOKEN ? " descend" :
            node.getMode() == EMPTY_TOKEN ? " lexical" : " dot"));
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        separate();
        if (node.args != null)
        {
            node.args.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, UnaryExpressionNode node)
    {
        indent();
        out.write("unary");
        push_in();
        out.write(Token.getTokenClassName(node.op));
        pop_out();
        separate();
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, BinaryExpressionNode node)
    {
        indent();
        out.write("binary");
        push_in();
        out.write(Token.getTokenClassName(node.op));
        pop_out();
        separate();
        push_in();
        if (node.lhs != null)
        {
            node.lhs.evaluate(cx, this);
        }
        separate();
        if (node.rhs != null)
        {
            node.rhs.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ConditionalExpressionNode node)
    {
        indent();
        out.write("cond");
        push_in();
        if (node.condition != null)
        {
            node.condition.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.thenexpr != null)
        {
            node.thenexpr.evaluate(cx, this);
        }
        separate();
        if (node.elseexpr != null)
        {
            node.elseexpr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ArgumentListNode node)
    {
        indent();
        out.write("argumentlist");

        push_in();

        for (Node n : node.items)
        {
            n.evaluate(cx, this);
            separate();
        }

        pop_out();

        return null;
    }

    public Value evaluate(Context cx, ListNode node)
    {
        indent();
        out.write("list");

        push_in();

        for (Node n : node.items)
        {
            n.evaluate(cx, this);
            separate();
        }

        pop_out();

        return null;
    }

    // Statements

    public Value evaluate(Context cx, StatementListNode node)
    {
        indent();
        //out.write("statementlist");
        push_in();
        out.writeln("{");

        for (Node n : node.items)
        {
            if (n != null)
            {
                n.evaluate(cx, this);
            }
        }

        pop_out();
        
        indent();
        out.writeln("}");

        return null;
    }

    public Value evaluate(Context cx, EmptyElementNode node)
    {
        indent();
        out.write("empty");
        return null;
    }

    public Value evaluate(Context cx, EmptyStatementNode node)
    {
        indent();
        out.write("empty");
        return null;
    }

    public Value evaluate(Context cx, ExpressionStatementNode node)
    {
        indent();
        out.write("expression");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, SuperStatementNode node)
    {
        indent();
        out.write("super");
        push_in();
        if (node.call.args != null)
        {
            node.call.args.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, LabeledStatementNode node)
    {
        indent();
        out.write("labeled");
        push_in();
        if (node.label != null)
        {
            node.label.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statement != null)
        {
            node.statement.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, IfStatementNode node)
    {
        indent();
        out.write("if");
        push_in();
        if (node.condition != null)
        {
            node.condition.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.thenactions != null)
        {
            node.thenactions.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.elseactions != null)
        {
            node.elseactions.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, SwitchStatementNode node)
    {
        indent();
        out.write("switch");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, CaseLabelNode node)
    {
        indent();
        out.write("case");
        push_in();
        if (node.label != null)
        {
            node.label.evaluate(cx, this);
        }
        else
        {
            out.write("default");
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, DoStatementNode node)
    {
        indent();
        out.write("do");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, WhileStatementNode node)
    {
        indent();
        out.write("while");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statement != null)
        {
            node.statement.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ForStatementNode node)
    {
        indent();
        out.write("for");
        push_in();
        if (node.initialize != null)
        {
            node.initialize.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.test != null)
        {
            node.test.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.increment != null)
        {
            node.increment.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statement != null)
        {
            node.statement.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, WithStatementNode node)
    {
        indent();
        out.write("with");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statement != null)
        {
            node.statement.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ContinueStatementNode node)
    {
        indent();
        out.write("continue");
        push_in();
        if (node.id != null)
        {
            node.id.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, BreakStatementNode node)
    {
        indent();
        out.write("break");
        push_in();
        if (node.id != null)
        {
            node.id.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ReturnStatementNode node)
    {
        indent();
        out.write("return");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ThrowStatementNode node)
    {
        indent();
        out.write("throw");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, TryStatementNode node)
    {
        indent();
        out.write("try");
        push_in();
        if (node.tryblock != null)
        {
            node.tryblock.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.catchlist != null)
        {
            node.catchlist.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.finallyblock != null)
        {
            node.finallyblock.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, CatchClauseNode node)
    {
        indent();
        out.write("catch");
        push_in();
        if (node.parameter != null)
        {
            node.parameter.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, FinallyClauseNode node)
    {
        indent();
        out.write("finally");
        push_in();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, UseDirectiveNode node)
    {
        indent();
        out.write("use");
        if( node.expr != null )
        {
            node.expr.evaluate(cx,this);
        }
        return null;
    }

    public Value evaluate(Context cx, IncludeDirectiveNode node)
    {
        indent();
        out.write("include");
        push_in();
        if (node.filespec != null)
        {
            node.filespec.evaluate(cx, this);
        }
        separate();
        if (node.program != null)
        {
            // node.program.evaluate(cx, this);
        }
        pop_out();

        return null;
    }

    // Definitions

    public Value evaluate(Context cx, ImportDirectiveNode node)
    {
        indent();
        out.write("import");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        pop_out();

        return null;
    }

    public Value evaluate(Context cx, AttributeListNode node)
    {
        indent();
        out.write("attributelist");

        push_in();

        for (Node n : node.items)
        {
            n.evaluate(cx, this);
            separate();
        }

        pop_out();

        return null;
    }

    public Value evaluate(Context cx, VariableDefinitionNode node)
    {
        indent();
        if (node.kind == CONST_TOKEN)
        {
            out.write("const");
        }
        else
        {
            out.write("var");
        }
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.list != null)
        {
            node.list.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, VariableBindingNode node)
    {
        indent();
        out.write("variablebinding");
        push_in();
        if (node.variable != null)
        {
            node.variable.evaluate(cx, this);
        }
        separate();
        if (node.initializer != null)
        {
            node.initializer.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, UntypedVariableBindingNode node)
    {
        indent();
        out.write("untypedvariablebinding");
        push_in();
        if (node.identifier != null)
        {
            node.identifier.evaluate(cx, this);
        }
        separate();
        if (node.initializer != null)
        {
            node.initializer.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, TypedIdentifierNode node)
    {
        indent();
        out.write("typedidentifier");
        push_in();
        if (node.identifier != null)
        {
            node.identifier.evaluate(cx, this);
        }
        separate();
        if (node.type != null)
        {
            node.type.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, BinaryFunctionDefinitionNode node)
    {
        return null;
    }

    public Value evaluate(Context cx, FunctionDefinitionNode node)
    {
        indent();
        out.write("function");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        separate();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        separate();
        if (node.fexpr != null)
        {
            node.fexpr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, FunctionNameNode node)
    {
        indent();
        out.write("functionname");
        out.write((node.kind == GET_TOKEN ? " get" :
            node.kind == SET_TOKEN ? " set" : ""));
        push_in();
        if (node.identifier != null)
        {

            node.identifier.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, FunctionSignatureNode node)
    {
        indent();
        out.write(node.inits != null ? "constructorsignature" : "functionsignature" );
        push_in();
        if (node.parameter != null)
        {
            node.parameter.evaluate(cx, this);
        }
        separate();
        if (node.result != null)
        {
            node.result.evaluate(cx, this);
        }
        if (node.inits != null)
        {
        	node.inits.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ParameterNode node)
    {
        indent();
        out.write("parameter");
        if (node.kind == CONST_TOKEN)
        {
            out.write(" const");
        }
        push_in();
        if (node.identifier != null)
        {
            node.identifier.evaluate(cx, this);
        }
        separate();
        if (node.init != null)
        {
            node.init.evaluate(cx, this);
        }
        separate();
        if (node.type != null)
        {
            node.type.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, RestExpressionNode node)
    {
        indent();
        out.write("restexpression");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, RestParameterNode node)
    {
        indent();
        out.write("restparameter");
        push_in();
        if (node.parameter != null)
        {
            node.parameter.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ClassDefinitionNode node)
    {
        indent();
        out.write("class");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        separate();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        separate();
        if (node.baseclass != null)
        {
            node.baseclass.evaluate(cx, this);
        }
        separate();
        if (node.interfaces != null)
        {
            node.interfaces.evaluate(cx, this);
        }
        separate();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, InterfaceDefinitionNode node)
    {
        indent();
        out.write("interface");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        separate();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        separate();
        if (node.interfaces != null)
        {
            node.interfaces.evaluate(cx, this);
        }
        separate();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ClassNameNode node)
    {
        indent();
        out.write("classname");
        push_in();
        if (node.pkgname != null)
        {
            node.pkgname.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.ident != null)
        {
            node.ident.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, InheritanceNode node)
    {
        if (node.baseclass != null)
        {
            indent();
            out.write("extends");
            push_in();
            node.baseclass.evaluate(cx, this);
            pop_out();
        }
        separate();
        if (node.interfaces != null)
        {
            indent();
            out.write("implements");
            push_in();
            node.interfaces.evaluate(cx, this);
            pop_out();
        }
        return null;
    }

    public Value evaluate(Context cx, NamespaceDefinitionNode node)
    {
        indent();
        out.write("namespace");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.value != null)
        {
            node.value.evaluate(cx, this);
        }
        pop_out();
        return null;
    }
    public Value evaluate(Context cx, ConfigNamespaceDefinitionNode node)
    {
        indent();
        out.write("config namespace");
        push_in();
        if (node.attrs != null)
        {
            node.attrs.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        pop_out();
        separate();
        push_in();
        if (node.value != null)
        {
            node.value.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, PackageDefinitionNode node)
    {
        indent();
        out.write("package");
        push_in();
        if (node.name != null)
        {
            node.name.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, PackageIdentifiersNode node)
    {
        indent();
        out.write("packageidentifiers");
        push_in();

        for (IdentifierNode n : node.list)
        {
            n.evaluate(cx, this);
            separate();
        }

        pop_out();
        return null;
    }

    public Value evaluate(Context cx, PackageNameNode node)
    {
        indent();
        out.write("packagename");
        push_in();
        if (node.id != null)
        {
            node.id.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, ProgramNode node)
    {
        indent();
        out.write("program");
        push_in();
        if (node.statements != null)
        {
            node.statements.evaluate(cx, this);
        }
        pop_out();
        out.writeln();
        return null;
    }

    public Value evaluate(Context cx, ErrorNode node)
    {
        indent();
        out.write("error");
        out.write(node.errorCode);
        return null;
    }

    public Value evaluate(Context cx, ToObjectNode node)
    {
        indent();
        out.write("toobject");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        out.writeln();
        return null;
    }

    public Value evaluate(Context cx, LoadRegisterNode node)
    {
        indent();
        out.write("loadregister");
        push_in();
        if (node.reg != null)
        {
            node.reg.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, StoreRegisterNode node)
    {
        indent();
        out.write("storeregister");
        push_in();
        if (node.reg != null)
        {
            node.reg.evaluate(cx, this);
        }
        pop_out();
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        out.writeln();
        return null;
    }

    public Value evaluate(Context cx, RegisterNode node)
    {
        indent();
        out.write("register");

        push_in();
        out.write(node.index);
        return null;
    }

    public Value evaluate(Context cx, HasNextNode node)
    {
        indent();
        out.write("hasNext");

        push_in();
        if (node.objectRegister != null)
        {
            node.objectRegister.evaluate(cx, this);
        }
        pop_out();
        push_in();
        if (node.indexRegister != null)
        {
            node.indexRegister.evaluate(cx, this);
        }
        pop_out();
        out.writeln();
        return null;
    }
    
    public Value evaluate(Context cx, BoxNode node)
    {
        indent();
        out.write("box");
        out.write(node.actual);
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, CoerceNode node)
    {
        indent();
        out.write("coerce");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, PragmaNode node)
    {
        indent();
        out.write("pragma");
        push_in();
        if (node.list != null)
        {
            node.list.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate(Context cx, UsePrecisionNode node)
    {
        indent();
        out.write("usePrecision(" + node.precision + ")");
        return null;
    }

    private static String[] usageName = {"Number", "decimal", "double", "int", "uint"};
    public Value evaluate(Context cx, UseNumericNode node)
    {
        indent();
        out.write("useNumeric(" + usageName[node.numeric_mode] + ")");
        return null;
    }

    public Value evaluate(Context cx, UseRoundingNode node)
    {
        indent();
        out.write("useRounding(" + NumberUsage.roundingModeName[node.mode] + ")");
        return null;
    }

    public Value evaluate(Context cx, PragmaExpressionNode node)
    {
        indent();
        out.write("pragmaitem");
        push_in();
        if (node.identifier != null)
        {
            node.identifier.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate( Context cx, ParameterListNode node )
    {
        indent();
        out.write("parameterlist");

        push_in();

        for (int i = 0, size = node.items.size(); i < size; i++)
        {
            ParameterNode param = node.items.get(i);

            if (param != null)
            {
                param.evaluate(cx, this);
            }

            if (i < size - 1)
            {
                separate();
            }
        }

        pop_out();

        return null;
    }

    public Value evaluate(Context cx, MetaDataNode node)
    {
        if (node.data != null)
        {
            MetaDataEvaluator mde = new MetaDataEvaluator();
            mde.evaluate(cx, node);
        }

        indent();
        out.write("metadata:");
        out.write(node.getId() !=null? node.getId() :"");
        out.write(" ");
        for (int i = 0, length = (node.getValues() == null) ? 0 : node.getValues().length; i < length; i++)
        {
            Value v = node.getValues()[i];
            if (v instanceof MetaDataEvaluator.KeyValuePair)
            {
                MetaDataEvaluator.KeyValuePair pair = (MetaDataEvaluator.KeyValuePair) v;
                out.write("[" + pair.key + "," + pair.obj + "]");
            }

            if (v instanceof MetaDataEvaluator.KeylessValue)
            {
                MetaDataEvaluator.KeylessValue val = (MetaDataEvaluator.KeylessValue) v;
                out.write("[" + val.obj + "]");
            }
        }
        return null;
    }

    public Value evaluate(Context cx, DefaultXMLNamespaceNode node)
    {
        indent();
        out.write("dxns");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }

    public Value evaluate( Context cx, DocCommentNode node )   
    { 
        evaluate(cx,(MetaDataNode)node);
        return null;
    }

    public Value evaluate( Context cx, ImportNode node )
    {
        return null;
    }

    public Value evaluate( Context cx, BinaryProgramNode node )
    {
        return null;
    }

    public Value evaluate(Context cx, BinaryClassDefNode node)
    {
        return null;
    }

    public Value evaluate(Context cx, BinaryInterfaceDefinitionNode node)
    {
        return null;
    }

    public Value evaluate(Context cx, TypeExpressionNode node)
    {
        indent();
        out.write("typeexpr");
        push_in();
        if (node.expr != null)
        {
            node.expr.evaluate(cx, this);
        }
        pop_out();
        return null;
    }
}