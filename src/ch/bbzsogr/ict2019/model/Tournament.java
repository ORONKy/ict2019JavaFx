/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.model;

/**
 *
 * @author varot
 */
public class Tournament {
    private int id;
    private String title;
    private String game;
    private String winner;
    private int tournamentSize;
    private int tournamentState;

    public Tournament ( int id, String title, String game, String winner, int tournamentSize )
    {
        this.id = id;
        this.title = title;
        this.game = game;
        this.winner = winner;
        this.tournamentSize = tournamentSize;
    }

    public Tournament ( int id, String title, String game, String winner, int tournamentSize, int tournamentState )
    {
        this.id = id;
        this.title = title;
        this.game = game;
        this.winner = winner;
        this.tournamentSize = tournamentSize;
        this.tournamentState = tournamentState;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    public int getId ()
    {
        return id;
    }

    public int getTournamentSize ()
    {
        return tournamentSize;
    }

    public String getWinner() {
        if(winner != null)
        return winner;
        return "undecided";
    }

    public int getTournamentState(){
        return tournamentState;
    }
}
