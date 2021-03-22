package ch.bbzsogr.ict2019.model;

public class Participant
{
	private int id;
	private String name;

	public Participant ( int id, String name )
	{
		this.id = id;
		this.name = name;
	}

	public int getId ()
	{
		return id;
	}

	public String getName ()
	{
		return name;
	}
}
