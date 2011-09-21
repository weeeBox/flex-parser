package code;

import java.io.ByteArrayOutputStream;

public class ASCodeBlock
{
	private ByteArrayOutputStream stream;
	
	public ASCodeBlock()
	{
		stream = new ByteArrayOutputStream();
	}
	
	public ByteArrayOutputStream getStream()
	{
		return stream;
	}
}
