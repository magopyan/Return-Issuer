package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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
            stage.getIcons().add(new Image("/icon.png"));
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
            stage.getIcons().add(new Image("/icon.png"));
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

    public static void openAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }
}
