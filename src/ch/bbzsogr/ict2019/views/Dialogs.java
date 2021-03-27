package ch.bbzsogr.ict2019.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Dialogs
{
	/**
	 * Can't access the Constructor
	 */
	private Dialogs ()
	{
	}

	/**
	 * Shows an Warning alert with ok Button
	 *
	 * @param message message which should be shown
	 * @param title title which should be shown
	 */
	public static void createWarningAlert ( String message, String title )
	{
		Alert alert = new Alert( Alert.AlertType.WARNING, message, ButtonType.OK );
		alert.setTitle( title );
		alert.showAndWait();
	}

	/**
	 * Shows a simple Error alert
	 * @param message message which should be shown
	 */
	public static void createErrorAlert ( String message )
	{
		Alert alert = new Alert( Alert.AlertType.ERROR, message, ButtonType.OK );
		alert.showAndWait();
	}
}
