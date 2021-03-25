/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Tournament;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.naming.Binding;

/**
 *
 * @author varot
 */
public class TournamentList {
    private ObservableList<Tournament> observableList;
    private GridPane gridPane;
    private Stage primaryStage;
    private Button viewBtn;
    private Button addTournamentBtn;
    private TableView tableView;
    private Image img;
    private ImageView imgView;
    
    private final String WINDOW_TITLE = "Tournament List";

    public TournamentList(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(WINDOW_TITLE);
        gridPane = new GridPane();
        initWindow();
        initTable();


        viewBtn.disableProperty().bind(Bindings.isEmpty( tableView.getSelectionModel().getSelectedItems())  );
        
        gridPane.add(imgView, 0,0, 2,1);
        gridPane.add(tableView, 0,1, 2,1);
        gridPane.add(viewBtn, 0 , 2);
        gridPane.add(addTournamentBtn, 1, 2);
        
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initWindow(){
        viewBtn = new Button("view");
        addTournamentBtn = new Button("+ Tournament");
        GridPane.setHalignment(addTournamentBtn, HPos.RIGHT);
        
        img = new Image("header.png");
        imgView = new ImageView(img);
        
    }
    
    private void initTable(){
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Integer, Tournament> column1 = new TableColumn<>("title");
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Integer, Tournament> column2 = new TableColumn<>("game");
        column2.setCellValueFactory(new PropertyValueFactory<>("game"));
        
        TableColumn<Integer, Tournament> column3 = new TableColumn<>("winner");
        column3.setCellValueFactory(new PropertyValueFactory<>("winner"));
        
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
    }
    
    public void populateTable( List<Tournament> al) {
        observableList = FXCollections.observableArrayList(al);
        tableView.setItems(observableList);
    }
    
    public void addActions( EventHandler<ActionEvent> evh) {
        viewBtn.setOnAction( evh );
        addTournamentBtn.setOnAction(evh);
    }

    public Button getViewBtn ()
    {
        return viewBtn;
    }

    public Button getAddTournamentBtn ()
    {
        return addTournamentBtn;
    }
    public Tournament getSelectedTournament(){
        return (Tournament) tableView.getSelectionModel().getSelectedItem();
    }
}
