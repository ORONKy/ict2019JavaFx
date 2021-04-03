package ch.bbzsogr.ict2019.event;

import javafx.event.Event;
import javafx.event.EventType;

public class MatchesEvent extends Event implements ICustomEvent
{
	private int matcheId;
	public static final EventType<MatchesEvent> CUSTOM = new EventType(ANY, "CUSTOM");

	public MatchesEvent ( int matchId )
	{
		super( MatchesEvent.CUSTOM );
		this.matcheId = matcheId;
	}

	@Override
	public int getMatcheId ()
	{
		return matcheId;
	}
}
