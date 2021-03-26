package ch.bbzsogr.ict2019.model;

public class Participant
{
	private int id;
	private String name;
	private boolean temporary;
	private boolean team;

	public Participant ( int id, String name, boolean temporary, boolean team )
	{
		this.id = id;
		this.name = name;
		this.temporary = temporary;
		this.team = team;
	}

	public int getId ()
	{
		return id;
	}

	public String getName ()
	{
		return name;
	}

	public boolean isTeam ()
	{
		return team;
	}

	public boolean isTemporary ()
	{
		return temporary;
	}
}
