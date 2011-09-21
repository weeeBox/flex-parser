package code;

import java.util.ArrayList;
import java.util.List;

public class ClassRecord extends TopLevelItemRecord
{
	protected String mName;	
	protected String mExtends;
	protected List<String> mImplements;	
	
	public ClassRecord(String name)
	{
		mName = name;
		mImplements = new ArrayList<String>();
	}
	
	public void setExtends(String name)
	{
		mExtends = name;
	}
	
	public String getExtends()
	{
		return mExtends;
	}
	
	public void addImplements(String name)
	{
		mImplements.add(name);
	}

	public List<String> getImplements()
	{
		return mImplements;
	}
}
