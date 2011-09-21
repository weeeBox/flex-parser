package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class WriteDestination 
{
	private PrintStream out;
	private StringBuilder tabs;
	private String tab;
	private boolean needTab;

	public WriteDestination(File file) throws IOException
	{
		this(new PrintStream(file));
	}
	
	public WriteDestination(PrintStream out) 
	{
		this.out = out;
		needTab = true;
		tabs = new StringBuilder();
		setTab("\t");
	}
	
	public void setTab(String tab)
	{
		this.tab = tab;
	}
	
	public void write(Object o)
	{
		if (needTab)
		{
			needTab = false;
			out.print(tabs);
		}
		out.print(o);
	}
	
	public void writeln(Object o)
	{
		write(o + "\n");
		needTab = true;
	}
	
	public void writeln()
	{
		writeln("");
	}
	
	public void incTab()
	{
		tabs.append(tab);
	}
	
	public void decTab()
	{
		if (tabs.length() == 0)
			throw new RuntimeException("Unable to dec tabs");
		
		tabs.setLength(tabs.length() - 1);
	}
	
	public void close() throws IOException
	{
		out.close();
	}
}
