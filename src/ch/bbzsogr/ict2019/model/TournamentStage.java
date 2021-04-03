package ch.bbzsogr.ict2019.model;

import java.util.List;

public class TournamentStage
{
	private List<Match> matches;
	private int stageNr;
	private boolean finished;
	private int participantsCount;

	public TournamentStage ( List<Match> matches, int stageNr, boolean finished, int participantsCount )
	{
		this.matches = matches;
		this.stageNr = stageNr;
		this.finished = finished;
		this.participantsCount = participantsCount;
	}

	public List<Match> getMatches ()
	{
		return matches;
	}

	public int getStageNr ()
	{
		return stageNr;
	}

	public boolean isFinished ()
	{
		return finished;
	}

	public int getParticipantsCount ()
	{
		return participantsCount;
	}
}
