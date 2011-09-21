package code;

import java.util.ArrayList;
import java.util.List;

public class ASClassDeclaration extends ASDeclaration
{
	private String packageName;
	private String superClass;

	private List<String> imports;
	private List<String> interfaces;
	
	private List<ASMemberDeclaration> members;
	private List<ASFunctionDeclaration> functions;
	
	private String body;
	
	public ASClassDeclaration(String name)
	{
		super(name, name);
		imports = new ArrayList<String>();
		interfaces = new ArrayList<String>();
		members = new ArrayList<ASMemberDeclaration>();
		functions = new ArrayList<ASFunctionDeclaration>();
	}

	public void addImport(String name)
	{
		imports.add(name);
	}
	
	public void addInterface(String name)
	{
		interfaces.add(name);
	}

	public void addMember(ASMemberDeclaration member)
	{
		members.add(member);
	}
	
	public void addFunction(ASFunctionDeclaration function)
	{
		functions.add(function);
	}

	public List<String> getImports()
	{
		return imports;
	}
	
	public List<String> getInterfaces()
	{
		return interfaces;
	}
	
	public List<ASMemberDeclaration> getMembers()
	{
		return members;
	}
	
	public List<ASFunctionDeclaration> getFunctions()
	{
		return functions;
	}
	
	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	
	public boolean hasSuperClass()
	{
		return superClass != null;
	}
	
	public String getSuperClass()
	{
		return superClass;
	}

	public void setSuperClass(String superClass)
	{
		this.superClass = superClass;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
	
	public String getBody()
	{
		return body;
	}
}
