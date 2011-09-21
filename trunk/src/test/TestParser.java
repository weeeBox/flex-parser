package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import macromedia.asc.parser.Parser;
import macromedia.asc.parser.ProgramNode;
import macromedia.asc.util.Context;
import macromedia.asc.util.ContextStatics;

public class TestParser
{
	public static void main(String[] args)
	{
		File file = new File(args[0]);
		
		try
		{
			ContextStatics statics = new ContextStatics();
			Context cx = new Context(statics);
			Parser parser = new Parser(cx, new FileInputStream(file), file.getPath());
			
			ProgramNode programNode = parser.parseProgram();			
			NodePrinter printer = new NodePrinter();
			printer.evaluate(cx, programNode);
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
