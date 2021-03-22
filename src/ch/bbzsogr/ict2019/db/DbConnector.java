/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.db;

import ch.bbzsogr.ict2019.model.Game;
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
				"ELECT tournament.ID AS id ,tournament.Title AS title , game.Name AS game, participant.Name AS name, tournament.size AS size "
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
}
