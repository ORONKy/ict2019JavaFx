/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.db;

import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Match;
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

	public DbConnector () throws SQLException
	{
		String baseurl = "jdbc:mysql://localhost/";
		String user = "root";
		String password = "";

		conn = DriverManager.getConnection( baseurl + "ictskills", user, password );
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

	public void addAllMatches ( int tournamentId, List<Match> matches, int stage ) throws SQLException
	{
		for ( int i = 0; i < matches.size(); i++ )
		{
			addMatch( tournamentId, matches.get( i ), stage, i );
		}
	}

	public void addMatch ( int tournamentId, Match match, int stage, int order ) throws SQLException
	{
		String sql = "INSERT INTO `match`(TournamentID, Participant1ID, Participant2ID, Stage, `Order`) VALUE (?,?,?,?,?)";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, tournamentId );
		preparedStatement.setInt( 2, match.getParticipant1().getId() );
		preparedStatement.setInt( 3, match.getParticipant2().getId() );
		preparedStatement.setInt( 4, stage );
		preparedStatement.setInt( 5, order );
		preparedStatement.executeUpdate();
	}

	public List<Match> readMatches ( int tournamentId, int stage )
	{
		List<Match> returnValue = new ArrayList<>();
		ResultSet resultSet;
		PreparedStatement statement;

		String sql = "Select match.ID             as id, " + "       `match`.TournamentID as tid, "
				+ "       `match`.Stage        as stage, " + "       `match`.`Order`, "
				+ "       `match`.WinnerParticipantID as winner, " + "       p1.ID                as p1id, "
				+ "       p1.Name              as p1name, " + "       p1.IsTemporary       as p1temporary, "
				+ "       p1.IsTeam            as p1team, " + "       p2.ID                as p2id, "
				+ "       p2.Name              as p2name, " + "       p2.IsTemporary       as p2temporary, "
				+ "       p2.IsTeam            as p2team " + "FROM `match` "
				+ "         LEFT JOIN participant p1 on p1.ID = `match`.Participant1ID "
				+ "         LEFT JOIN participant p2 on p2.ID = `match`.Participant2ID "
				+ "WHERE `match`.TournamentID = ? " + "AND `match`.Stage = ? ;";

		try
		{
			statement = conn.prepareStatement( sql );
			statement.setInt( 1, tournamentId );
			statement.setInt( 2, stage );
			statement.executeQuery();
			resultSet = statement.getResultSet();

			while ( resultSet.next() )
			{
				Participant p1 = new Participant( resultSet.getInt( "p1id" ), resultSet.getString( "p1name" ),
						resultSet.getInt( "p1temporary" ) == 1, resultSet.getInt( "p1team" ) == 1 );
				Participant p2 = new Participant( resultSet.getInt( "p2id" ), resultSet.getString( "p2name" ),
						resultSet.getInt( "p2temporary" ) == 1, resultSet.getInt( "p2team" ) == 1 );
				Participant winner =
						resultSet.getInt( "winner" ) == 0 ? null : resultSet.getInt( "winner" ) == p1.getId() ? p1 : p2;
				returnValue.add( new Match( resultSet.getInt( "id" ), resultSet.getInt( "tid" ), p1, p2,
						resultSet.getShort( "stage" ), resultSet.getInt( "order" ), winner ) );
			}
		}
		catch ( SQLException throwables )
		{
			throwables.printStackTrace();
		}
		return returnValue;
	}

	public int readTournamentState ( int tournamentId ) throws SQLException
	{
		ResultSet resultSet;
		PreparedStatement preparedStatement;

		String sql = "Select TournamentState as state " + "From tournament " + "WHERE ID = ?";
		preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, tournamentId );
		preparedStatement.executeQuery();
		resultSet = preparedStatement.getResultSet();
		if ( resultSet.next() )
		{
			return resultSet.getInt( "state" );
		}
		return 0;
	}

	public void updateTournamentState ( int tournamentId, int state ) throws SQLException
	{
		String sql = "UPDATE tournament SET TournamentState = ? " + "WHERE ID = ? ";
		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, state );
		preparedStatement.setInt( 2, tournamentId );
		preparedStatement.executeUpdate();
	}

	public void setMatchWinner ( int winnerParticipant, int matchId ) throws SQLException
	{
		String sql = "UPDATE `match` SET `match`.WinnerParticipantID = ? " + "WHERE ID = ?";
		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, winnerParticipant );
		preparedStatement.setInt( 2, matchId );
		preparedStatement.executeUpdate();
	}

	public List<Participant> readStageWinner ( int tournamentId, int stage ) throws SQLException
	{
		List<Participant> returnValue = new ArrayList<>();

		String sql = "SELECT p.ID as id, p.Name as name, p.IsTemporary as temporary, p.IsTeam as team from `match` m "
				+ "JOIN participant p on p.ID = m.WinnerParticipantID " + "WHERE m.Stage = ? AND m.TournamentID = ?";

		PreparedStatement preparedStatement = conn.prepareStatement( sql );
		preparedStatement.setInt( 1, stage );
		preparedStatement.setInt( 2, tournamentId );

		preparedStatement.executeQuery();

		ResultSet resultSet = preparedStatement.getResultSet();

		while ( resultSet.next() )
		{
			returnValue.add( new Participant( resultSet.getInt( "id" ), resultSet.getString( "name" ),
					resultSet.getInt( "temporary" ) == 1, resultSet.getInt( "team" ) == 1 ) );
		}

		return returnValue;
	}

	public void writeMatchWinner(int winnerParticipantId, int tournamentId) throws SQLException
	{
		String sql = "UPDATE tournament SET tournament.WinnerParticipantID = ? WHERE tournament.ID = ?";

		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt( 1, winnerParticipantId );
		preparedStatement.setInt( 2, tournamentId );
		preparedStatement.executeUpdate();
	}
}
