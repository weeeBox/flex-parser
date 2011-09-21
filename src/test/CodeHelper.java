package test;

import macromedia.asc.parser.Tokens;

public class CodeHelper
{
	public static String tokenName(int tokenClass)
	{
	    int temp = -1 * tokenClass;
		return Tokens.tokenToString[temp];
	}
	
	public static String identifier(String name)
	{
		return name;
	}
	
	public static String type(String name)
	{
		return name;
	}
}
