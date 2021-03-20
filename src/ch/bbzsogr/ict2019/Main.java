/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author varot
 */
public class Main extends Application{
    @Override
    public void start( Stage primaryStage ) throws Exception {
        new Controller( primaryStage);
      
    }

    public static void main(String[] args) {
        launch();
    }
}
