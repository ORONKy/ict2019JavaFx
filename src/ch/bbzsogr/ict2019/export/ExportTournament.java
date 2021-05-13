package ch.bbzsogr.ict2019.export;

import ch.bbzsogr.ict2019.model.Match;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExportTournament
{
	/**
	 * No access from outside
	 */
	private ExportTournament ()
	{
	}

	/**
	 * Create an CSV export of a list of matches
	 *
	 * @param data matches which should be exported
	 * @return was the export successfully
	 */
	public static boolean export ( List<Match> data, String absolutePath )
	{
		List<String[]> dataLines = data.stream()
				.map( match -> new String[] { String.valueOf( match.getStageNr() ), String.valueOf( match.getOrder() ),
						match.getWinnerParticipantId().getName(),
						match.getWinnerParticipantId().getId() != match.getParticipant1().getId() ?
								match.getParticipant1().getName() :
								match.getParticipant2() != null ? match.getParticipant2().getName() : "-" } )
				.collect( Collectors.toList() );
		dataLines.add( 0, new String[] { "Stage", "Order", "Winner", "Loser" } );
		try
		{
			new ExportTournament().writeCSV( dataLines, absolutePath );
			return true;
		}
		catch ( FileNotFoundException e )
		{
			System.err.println( e );
			return false;
		}
	}

	private String convertToCSV ( String[] data )
	{
		return Stream.of( data ).collect( Collectors.joining( "," ) );
	}

	private void writeCSV ( List<String[]> dataLines, String path ) throws FileNotFoundException
	{
		File csvOutputFile = new File( path );
		PrintWriter pw = new PrintWriter( csvOutputFile );
		dataLines.stream().map( this::convertToCSV ).forEach( pw::println );
		pw.close();

	}
}
