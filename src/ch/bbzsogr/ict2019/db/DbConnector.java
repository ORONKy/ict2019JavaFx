/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.db;

import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.Tournament;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author varot
 */
public class DbConnector
{
	private Connection conn;

	public DbConnector ()
	{
		String baseurl = "jdbc:mysql://localhost/";
		String user = "root";
		String password = "";

		try
		{
			conn = DriverManager.getConnection( baseurl + "ictskills", user, password );
		}
		catch ( Exception e )
		{
			System.err.println( e );
			System.exit( 0 );
		}
	}

	public List<Tournament> readTournament ()
	{
		Statement sqlStatement;
		ResultSet result;
		List<Tournament> returnValue = new ArrayList<>();

		String query =
				"SELECT tournament.ID AS id ,tournament.Title AS title , game.Name AS game, participant.Name AS name, tournament.size AS size "
						+ "FROM tournament " + "LEFT JOIN game ON game.ID = tournament.GameID "
						+ "LEFT JOIN participant ON participant.ID = tournament.WinnerParticipantID;";

		try
		{
			sqlStatement = conn.createStatement();
			sqlStatement.execute( query );
			result = sqlStatement.getResultSet();
			while ( result.next() )
			{
				returnValue.add( new Tournament( result.getInt( "id" ), result.getString( "title" ),
						result.getString( "game" ), result.getString( "name" ), result.getInt( "size" ) ) );

			}
		}
		catch ( Exception e )
		{
			System.out.println( e );
		}
		return returnValue;
	}

	public List<Game> readeGames ()
	{
		Statement sqlStatement;
		ResultSet result;
		List<Game> returnValue = new ArrayList<>();

		String query = "SELECT game.ID AS id, game.Name AS name FROM game;";

		try
		{
			sqlStatement = conn.createStatement();
			sqlStatement.execute( query );
			result = sqlStatement.getResultSet();
			while ( result.next() )
			{
				returnValue.add( new Game( result.getInt( "id" ), result.getString( "name" ) ) );

			}
		}
		catch ( Exception e )
		{
			System.out.println( e );
		}
		return returnValue;
	}

	public List<Participant> readParticipantsForTournament ( int tournamentId )
	{
		Statement sqlStatement;
		ResultSet result;
		List<Participant> returnValue = new ArrayList<>();

		String query =
				"SELECT participant.Name AS name, participant.ID AS id, participant.IsTemporary AS temporary, participant.IsTeam AS team "
						+ "FROM participantintournament "
						+ "LEFT JOIN participant ON participant.ID = participantintournament.ParticipantID "
						+ "WHERE participantintournament.TournamentID = " + tournamentId;

		try
		{
			sqlStatement = conn.createStatement();
			sqlStatement.execute( query );
			result = sqlStatement.getResultSet();
			while ( result.next() )
			{
				returnValue.add( new Participant( result.getInt( "id" ), result.getString( "name" ),
						result.getInt( "temporary" ) == 1, result.getInt( "team" ) == 1 ) );

			}
		}
		catch ( Exception e )
		{
			System.out.println( e );
		}
		return returnValue;
	}

	public void createTournament ( String name, int size, int gameId, int gameStand ) throws SQLException
	{
		String sql = "INSERT INTO tournament(Title, GameId, Size, TournamentState) " + "VALUES(?,?,?,?)";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setString( 1, name );
		preparedStatement.setInt( 2, gameId );
		preparedStatement.setInt( 3, size );
		preparedStatement.setInt( 4, gameStand );
		preparedStatement.executeUpdate();
	}

	public void editParticipant ( Participant participant ) throws SQLException
	{
		String sql = "UPDATE participant SET " + "participant.Name = ?, " + "participant.IsTemporary = ?, "
				+ "participant.IsTeam = ? " + "WHERE participant.ID = ?";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setString( 1, participant.getName() );
		preparedStatement.setInt( 2, participant.isTeam() ? 1 : 0 );
		preparedStatement.setInt( 3, participant.isTemporary() ? 1 : 0 );
		preparedStatement.setInt( 4, participant.getId() );
		preparedStatement.executeUpdate();
	}

	public void createAllParticipants ( List<Participant> participants, int tournamentId ) throws SQLException
	{
		for ( Participant p : participants )
		{
			createParticipant( p, tournamentId );
		}
	}

	public void addAllParticipantsToTournament ( List<Integer> participantsIds, int tournamentId ) throws SQLException
	{
		for ( Integer p : participantsIds )
		{
			addParticipantToTournament( p, tournamentId );
		}
	}

	public void createParticipant ( Participant participant, int tournamentId ) throws SQLException
	{
		int participantAutoId = 0;
		String sql = "INSERT INTO participant(NAME, isTeam, isTemporary) VALUES(?, ?, ?)";

		PreparedStatement preparedStatement = conn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
		preparedStatement.setString( 1, participant.getName() );
		preparedStatement.setInt( 2, participant.isTeam() ? 1 : 0 );
		preparedStatement.setInt( 3, participant.isTemporary() ? 1 : 0 );
		preparedStatement.executeUpdate();

		ResultSet res = preparedStatement.getGeneratedKeys();
		res.next();
		participantAutoId = res.getInt( 1 );
		addParticipantToTournament( participantAutoId, tournamentId );
	}

	public void addParticipantToTournament ( int patricianId, int tournamentId ) throws SQLException
	{
		String sql = "INSERT INTO participantintournament(TournamentID, ParticipantID) VALUES(?,?)";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, tournamentId );
		preparedStatement.setInt( 2, patricianId );
		preparedStatement.executeUpdate();
	}

	public Participant readParticipant ( Participant participant ) throws SQLException
	{
		Participant returnValue = null;
		String sql =
				"SELECT participant.ID AS id, participant.Name AS name, participant.IsTeam AS team, participant.IsTemporary AS temporary "
						+ "FROM participant " + "WHERE participant.Name = ? " + "AND participant.IsTeam = ? "
						+ "AND participant.IsTemporary = ? ";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setString( 1, participant.getName() );
		preparedStatement.setInt( 2, participant.isTeam() ? 1 : 0 );
		preparedStatement.setInt( 3, participant.isTemporary() ? 1 : 0 );
		preparedStatement.executeQuery();

		ResultSet res = preparedStatement.getResultSet();
		if ( res.next() )
		{
			returnValue = new Participant( res.getInt( "id" ), res.getString( "name" ), res.getInt( "team" ) == 1,
					res.getInt( "temporary" ) == 1 );
		}
		return returnValue;
	}

	public void removeParticipantFromTournament ( int tournamentId, int participantId ) throws SQLException
	{
		String sql = "DELETE FROM participantintournament WHERE participantintournament.TournamentID = ? AND participantintournament.ParticipantID = ?";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, tournamentId );
		preparedStatement.setInt( 2, participantId );
		preparedStatement.executeUpdate();
	}

	public void removeParticipant ( int participantId ) throws SQLException
	{
		String sqlRelation = "DELETE FROM participantintournament WHERE participantintournament.ParticipantID = ?";
		String sqlParticipant = "DELETE FROM participant WHERE ID = ?";

		PreparedStatement preparedStatement1 = conn.prepareStatement( sqlRelation );
		preparedStatement1.setInt( 1, participantId );
		preparedStatement1.executeUpdate();

		PreparedStatement preparedStatement2 = conn.prepareStatement( sqlParticipant );
		preparedStatement2.setInt( 1, participantId );
		preparedStatement2.executeUpdate();
	}
}
