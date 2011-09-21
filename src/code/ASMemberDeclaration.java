package code;

public class ASMemberDeclaration extends ASDeclaration
{
	private String visiblity;
	private boolean isStatic;
	private boolean isConst;

	public ASMemberDeclaration(String type, String name, String visiblity)
	{
		super(type, name);
		this.visiblity = visiblity;
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
