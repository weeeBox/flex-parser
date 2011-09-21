package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import code.ASClassDeclaration;
import code.ASFunctionDeclaration;
import code.ASMemberDeclaration;

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
			
			write(printer.getClasses());
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void write(List<ASClassDeclaration> classes) throws IOException
	{
		WriteDestination headerDest = new WriteDestination(new File("d:/dev/out.h"));
		WriteDestination implDest = new WriteDestination(new File("d:/dev/out.mm"));

		for (ASClassDeclaration aClass : classes)
		{
			write(aClass, headerDest, implDest);
		}
		
		headerDest.close();
		implDest.close();
	}

	private static void write(ASClassDeclaration aClass, IWriteDestination hdr, IWriteDestination impl)
	{
		String className = aClass.getName();
		String superType = aClass.hasSuperClass() ? aClass.getSuperClass() : "NSObject";
		
		hdr.writeln("@inteface " + className + " : " + superType);
		
		writeMembers(aClass, hdr);
		writeFunctions(aClass, hdr);

		hdr.writeln("@end");
		hdr.writeln();
	}

	private static void writeMembers(ASClassDeclaration aClass, IWriteDestination hdr)
	{
		List<ASMemberDeclaration> members = aClass.getMembers();
		if (members.size() > 0)
		{
			hdr.writeln("{");
			hdr.incTab();
			
			for (ASMemberDeclaration member : members)
			{
				hdr.writeln(member.getType() + " " + member.getName() + ";");
			}
			
			hdr.decTab();
			hdr.writeln("}");
		}
	}
	
	private static void writeFunctions(ASClassDeclaration aClass, IWriteDestination hdr)
	{
		List<ASFunctionDeclaration> functions = aClass.getFunctions();
		for (ASFunctionDeclaration func : functions)
		{
			boolean isStatic = func.isStatic();
			String name = func.getName(); 
			String type = func.getType(); type = type == null ? "void" : type;
			
			hdr.writeln(String.format("%s (%s)%s", isStatic ? "+" : "-", type, name));
		}
	}
}
