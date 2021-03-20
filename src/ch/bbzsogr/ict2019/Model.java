/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

/**
 *
 * @author varot
 */
public class Model {
    private int count;

    public Model() {
        count = 1;
    }

    public int getCount() {
        return count;
    }

    public void increase() {
       count++;
    }
}
