package ch.bbzsogr.ict2019.model;

import javafx.scene.control.Button;

public class Match
{
	private int id;
	private int tournamentId;
	private Participant participant1;
	private Participant Participant2;
	private int stageNr;
	private int order;
	private Participant winnerParticipantId;
	private Button participant1win;
	private Button participant1los;
	private Button participant2win;
	private Button participant2los;

	public Match ( int id, int tournamentId, Participant participant1, Participant participant2, int stageNr,
			int order, Participant winnerParticipantId )
	{
		this.id = id;
		this.tournamentId = tournamentId;
		this.participant1 = participant1;
		Participant2 = participant2;
		this.stageNr = stageNr;
		this.order = order;
		this.winnerParticipantId = winnerParticipantId;
	}

	public int getId ()
	{
		return id;
	}

	public int getTournamentId ()
	{
		return tournamentId;
	}

	public Participant getParticipant1 ()
	{
		return participant1;
	}

	public Participant getParticipant2 ()
	{
		return Participant2;
	}

	public int getStageNr ()
	{
		return stageNr;
	}

	public int getOrder ()
	{
		return order;
	}

	public Participant getWinnerParticipantId ()
	{
		return winnerParticipantId;
	}

	public Button getParticipant1win ()
	{
		return participant1win;
	}

	public void setParticipant1win ( Button participant1win )
	{
		this.participant1win = participant1win;
	}

	public Button getParticipant1los ()
	{
		return participant1los;
	}

	public void setParticipant1los ( Button participant1los )
	{
		this.participant1los = participant1los;
	}

	public Button getParticipant2win ()
	{
		return participant2win;
	}

	public void setParticipant2win ( Button participant2win )
	{
		this.participant2win = participant2win;
	}

	public Button getParticipant2los ()
	{
		return participant2los;
	}

	public void setParticipant2los ( Button participant2los )
	{
		this.participant2los = participant2los;
	}

	public void setWinnerParticipantId ( Participant winnerParticipantId )
	{
		this.winnerParticipantId = winnerParticipantId;
	}
}
