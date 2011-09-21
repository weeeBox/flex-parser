package code;

public class ASDeclaration extends ASNamed
{
	private String type;
	
	public ASDeclaration(String type, String name)
	{
		super(name);
		this.type = type;		
	}

	public String getType()
	{
		return type;
	}
}