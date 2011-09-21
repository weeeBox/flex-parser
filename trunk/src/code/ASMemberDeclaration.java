package code;

public class ASMemberDeclaration
{
	private String type;
	private String name;
	private String visiblity;
	private boolean isStatic;
	private boolean isConst;

	public ASMemberDeclaration(String type, String name, String visiblity)
	{
		this.type = type;
		this.name = name;
		this.visiblity = visiblity;
	}

	public String getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public String getVisiblity()
	{
		return visiblity;
	}

	public boolean isStatic()
	{
		return isStatic;
	}

	public void setStatic(boolean isStatic)
	{
		this.isStatic = isStatic;
	}

	public boolean isConst()
	{
		return isConst;
	}

	public void setConst(boolean isConst)
	{
		this.isConst = isConst;
	}
}
