package ch.bbzsogr.ict2019.model;

public class Participant
{
	private int id;
	private String name;
	private boolean temporary;
	private boolean team;
	private boolean newUser;

	/**
	 * Creates an user who is NOT an new user
	 * @param id id of the user
	 * @param name name of the user
	 * @param temporary true if this user is temporary
	 * @param team true if this user is in a team
	 */
	public Participant ( int id, String name, boolean temporary, boolean team )
	{
		this.id = id;
		this.name = name;
		this.temporary = temporary;
		this.team = team;
		this.newUser = false;
	}

	/**
	 * creates an user
	 * @param id id of the user
	 * @param name name of the user
	 * @param temporary true if this user is temporary
	 * @param team true if this user is in a team
	 * @param newUser true if this user is an not existing user jet
	 */
	public Participant ( int id, String name, boolean temporary, boolean team, boolean newUser )
	{
		this.id = id;
		this.name = name;
		this.temporary = temporary;
		this.team = team;
		this.newUser = newUser;
	}

	/**
	 * Creates an empty user
	 * @param newUser if true this is an not existing user jet
	 */
	public Participant(boolean newUser){
		this.newUser = newUser;
		id = 0;
		name = "";
		temporary = false;
		team = false;
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

	public boolean isNewUser(){
		return newUser;
	}
}
