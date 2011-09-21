package code;

import java.util.ArrayList;
import java.util.List;

public class ASFunctionDeclaration extends ASMemberDeclaration
{
	private List<ASDeclaration> params;
	
	private ASCodeBlock body;
	
	public ASFunctionDeclaration(String type, String name, String visiblity)
	{
		super(type, name, visiblity);
		params = new ArrayList<ASDeclaration>();
	}

	public void addParam(ASDeclaration param)
	{
		params.add(param);
	}

	public ASCodeBlock getBody()
	{
		return body;
	}

	public void setBody(ASCodeBlock body)
	{
		this.body = body;
	}
}
