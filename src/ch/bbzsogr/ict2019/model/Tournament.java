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
    private String title;
    private String game;
    private String winner;

    public Tournament(String title, String game, String winner) {
        this.title = title;
        this.game = game;
        this.winner = winner;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    public String getWinner() {
        if(winner != null)
        return winner;
        return "undecided";
    }
}
