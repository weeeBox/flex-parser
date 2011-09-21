package test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class WriteDestination implements IWriteDestination 
{
	private PrintStream out;
	private StringBuilder tabs;
	private String tab;
	private boolean needTab;

	public WriteDestination(File file) throws IOException
	{
		this(new PrintStream(file));
	}
	
	public WriteDestination(OutputStream stream) throws IOException
	{
		this(new PrintStream(stream));
	}
	
	public WriteDestination(PrintStream out) throws IOException
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
	
	/* (non-Javadoc)
	 * @see test.IWriteDestination#write(java.lang.Object)
	 */
	@Override
	public void write(Object o)
	{
		if (needTab)
		{
			needTab = false;
			out.print(tabs);
		}
		out.print(o);
	}
	
	/* (non-Javadoc)
	 * @see test.IWriteDestination#writeln(java.lang.Object)
	 */
	@Override
	public void writeln(Object o)
	{
		write(o + "\n");
		needTab = true;
	}
	
	/* (non-Javadoc)
	 * @see test.IWriteDestination#writeln()
	 */
	@Override
	public void writeln()
	{
		writeln("");
	}
	
	/* (non-Javadoc)
	 * @see test.IWriteDestination#incTab()
	 */
	@Override
	public void incTab()
	{
		tabs.append(tab);
	}
	
	/* (non-Javadoc)
	 * @see test.IWriteDestination#decTab()
	 */
	@Override
	public void decTab()
	{
		if (tabs.length() == 0)
			throw new RuntimeException("Unable to dec tabs");
		
		tabs.setLength(tabs.length() - 1);
	}
	
	public void close()
	{
		out.close();
	}
}
