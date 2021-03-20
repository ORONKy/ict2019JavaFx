/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.db;

import ch.bbzsogr.ict2019.model.Tournament;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author varot
 */
public class DbConnector {
    private Connection conn;
    
    public DbConnector() {
        String  baseurl = "jdbc:mysql://localhost/";     
        String  user = "root";
        String  password = "";        
                
        try {
            conn = DriverManager.getConnection(baseurl+"ictskills", user, password);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }
    }
    
    public List<Tournament> readTournament(){
        java.sql.Statement sqlStatement;
        ResultSet result;
        List<Tournament> returnValue = new ArrayList<>();
        
        String query = "SELECT tournament.Title AS title , game.Name AS game, participant.Name AS name" +
                        " FROM tournament" +
                        " LEFT JOIN game ON game.ID = tournament.GameID" +
                        " LEFT JOIN participant ON participant.ID = tournament.WinnerParticipantID;";
        
        try {
            sqlStatement = conn.createStatement();
            sqlStatement.execute(query); 
            result = sqlStatement.getResultSet(); 
            while (result.next()) {
                returnValue.add(new Tournament(result.getString("title"), result.getString("game"), result.getString("name")));
               
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnValue;
    }
}
