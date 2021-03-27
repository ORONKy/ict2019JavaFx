package ch.bbzsogr.ict2019.util;

import ch.bbzsogr.ict2019.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class FillRandomUtil
{
	private static final String TEMPLATE_NAME = "Participant #";
	private static final boolean IS_TEMPORARY = true;
	private static final boolean IS_TEAM = true;

	public static List<Participant> createTemporaryParticipants(int count)
	{
		List<Participant> returnValue = new ArrayList<>();
		for ( int i = 0; i < count; i++ )
		{
			returnValue.add( new Participant( 0, TEMPLATE_NAME+(i+1), IS_TEMPORARY, IS_TEAM, true) );
		}
		return returnValue;
	}
}
