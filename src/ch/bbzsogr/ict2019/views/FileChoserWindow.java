package ch.bbzsogr.ict2019.views;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChoserWindow
{
	FileChooser fileChooser;
	Stage stage;
	public FileChoserWindow ( Stage stage)
	{
		fileChooser = new FileChooser();
		this.stage = stage;
	}

	public File showWindow(){
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.setTitle( "Choose export location" );
		fileChooser.getExtensionFilters().add( extFilter );
		return fileChooser.showSaveDialog( stage );
	}
}
