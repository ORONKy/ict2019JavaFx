package ch.bbzsogr.ict2019.util;

import ch.bbzsogr.ict2019.model.Match;
import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.TournamentStage;
import ch.bbzsogr.ict2019.views.StageTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchesUtil
{
	private void createStage( List<Participant> participantList ){

	}

	/**
	 * Creates Random Matches out of a List of Participants
	 * @param participantList List with the Participants which are the member of the new matches
	 * @param tournamentId
	 * @param stageNr stageId
	 * @return List of Random matches
	 */
	public static List<Match> createMatches(List<Participant> participantList, int tournamentId, int stageNr)
	{
		List<Match> returnValue = new ArrayList<>();
		List<Participant> participants = new ArrayList<>(participantList);

		while ( participants.size() > 1 )
		{
			if ( participants.size() > 1 )
			{
				Participant participant1 = removeRandom( participants );
				Participant participant2 = removeRandom( participants );
				returnValue.add( new Match( 0, tournamentId, participant1, participant2, stageNr, 0, null  ) );
			}
		}
		if ( participants.size() > 0 ){
			returnValue.add( new Match( 0, tournamentId, participants.get( 0 ), null, stageNr,0, participants.get( 0 ) ) );
		}
		return returnValue;
	}

	/**
	 * Helper method for createMatches which removes a random Element of the input list.
	 * @param list
	 * @param <T>
	 * @return the removed Item
	 */
	private static <T> T removeRandom(List<T> list)
	{
		Random random = new Random();
		if ( list != null && list.size() > 0 )
		{
			int index = random.nextInt(list.size());
			T obj = list.remove( index );
			return obj;
		}
		return null;
	}

	public static StageTab createStageTab(int stage, List<Match> matches, boolean finished)
	{
		TournamentStage stageObj = new TournamentStage( matches, stage, finished, getTournamentParticipants( matches ) );
		StageTab stageTab = new StageTab( stageObj );
		stageTab.setText( "stage " + stage );
		return stageTab;
	}

	public static int getTournamentParticipants(List<Match> matches){
		List<Participant> participants = new ArrayList<>();
		matches.forEach( match -> {
			participants.add( match.getParticipant1() );
			participants.add( match.getParticipant2() );
		} );
		return (int) participants.stream().filter( participant -> participant != null ).count();
	}
}
