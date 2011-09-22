package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import code.ASClassDeclaration;
import code.ASDeclaration;
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
		File srcFile = new File(args[0]);
		File outputDir = new File(args[1]);
		
		try
		{
			ContextStatics statics = new ContextStatics();
			Context cx = new Context(statics);
			Parser parser = new Parser(cx, new FileInputStream(srcFile), srcFile.getPath());
			
			ProgramNode programNode = parser.parseProgram();			
			NodePrinter printer = new NodePrinter();
			printer.evaluate(cx, programNode);
			
			String filename = srcFile.getName();
			String moduleName = filename.substring(0, filename.lastIndexOf('.'));
			write(printer.getClasses(), outputDir, moduleName);
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void write(List<ASClassDeclaration> classes, File outputDir, String moduleName) throws IOException
	{
		WriteDestination headerDest = new WriteDestination(new File(outputDir, moduleName + ".h"));
		WriteDestination implDest = new WriteDestination(new File(outputDir, moduleName + ".m"));

		implDest.writeln("#import \"" + moduleName + ".h\"");
		implDest.writeln();
		
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
		
		impl.writeln("@implementation " + className);
		hdr.writeln("@inteface " + className + " : " + superType);
		
		impl.incTab();
		
		writeMembers(aClass, hdr);
		writeFunctions(aClass, hdr, impl);
		
		impl.decTab();

		hdr.writeln("@end");
		hdr.writeln();
		
		impl.writeln("@end");
		impl.writeln();
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
	
	private static void writeFunctions(ASClassDeclaration aClass, IWriteDestination hdr, IWriteDestination impl)
	{
		List<ASFunctionDeclaration> functions = aClass.getFunctions();
		for (ASFunctionDeclaration func : functions)
		{
			writeFunctionSignature(func, hdr);
			writeFunctionSignature(func, impl);
			
			hdr.writeln(";");
			impl.writeln();
		}
	}

	private static void writeFunctionSignature(ASFunctionDeclaration func, IWriteDestination dest)
	{
		boolean isStatic = func.isStatic();
		String name = func.getName(); 
		String type = func.getType();
		type = type == null ? (func.isConstructor() ? "id" : "void") : type;
		name = func.isConstructor() ? "init" : name;
		
		dest.write(String.format("%s (%s)%s", isStatic ? "+" : "-", type, name));
		List<ASDeclaration> params = func.getParams();
		if (params.size() > 0)
		{
			int paramIndex = 0;
			for (ASDeclaration param : params)
			{
				dest.write(":(" + param.getType() + ")" + param.getName());
				if (++paramIndex < params.size())
					dest.write(" ");
			}
		}
	}
}
