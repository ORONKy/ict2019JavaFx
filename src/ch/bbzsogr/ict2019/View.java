/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author varot
 */
public class View {

private Stage stage;
  private Label label;
  private Button countButton;

  public View( Stage stage ) {
    this.stage = stage;
    label = new Label();
    countButton = new Button("Count");
    stage.setScene(new Scene(new VBox(label, countButton)));
    updateLabel( 0 );
    stage.setResizable(false);
    stage.show();
  }

  public void updateLabel( int count ) {
    label.setText( "Count: " + count );
  }

  public Button getCountButton() {
    return countButton;
  }
  
  public void addActions( EventHandler<ActionEvent> evh) {
    countButton.setOnAction( evh );
  }
}
