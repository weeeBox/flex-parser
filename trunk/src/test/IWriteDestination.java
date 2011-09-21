package test;

public interface IWriteDestination
{

	public abstract void write(Object o);

	public abstract void writeln(Object o);

	public abstract void writeln();

	public abstract void incTab();

	public abstract void decTab();

	public abstract void close();

}