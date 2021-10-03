package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FormOpener {

    public static void openNewForm(String fxmlFileName, String title) {

        FXMLLoader fxmlLoad = null;
        try {
            fxmlLoad = new FXMLLoader(new FormOpener().getClass().getResource(fxmlFileName));

            Parent root = fxmlLoad.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't load new window. ");
        }
    }

    public static void openNewFormAndCloseOld(String fxmlFileName, String title, boolean background, ActionEvent event) {

        FXMLLoader fxmlLoad = null;
        try {
            fxmlLoad = new FXMLLoader(new FormOpener().getClass().getResource(fxmlFileName));

            Parent root = fxmlLoad.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setIconified(background);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't load new window. ");
        }

        closeForm(event);
    }

    private static void closeForm(ActionEvent event) {

        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public static void openAlert() {


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("BO login failed");
        alert.setHeaderText("Unable to log into BackOffice, please enter the right credentials!");
        alert.setContentText(null);
        alert.showAndWait();
    }
}
