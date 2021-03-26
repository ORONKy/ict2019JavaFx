package ch.bbzsogr.ict2019.util;

import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Participant;

import java.util.regex.Pattern;

public class ValidationUtil
{
	public final static String NO_WHITESPACE_PATTERN = ".*\\w.*";

	public static boolean validateCreateTournament ( String name, int size, Game game )
	{
		//TODO not implemented yet
		return game != null;
	}

	public static boolean validateUser ( Participant participant )
	{
		if ( participant != null )
		{
			if ( participant.getName() != null && participant.getName() != "" && Pattern
					.matches( NO_WHITESPACE_PATTERN, participant.getName() ) )
				return true;
		}
		return false;
	}
}
