package controllers;

import backoffice_credentials.BackOfficeUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.FormOpener;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;


    public void onSave(ActionEvent event) throws IOException {

        BackOfficeUser BOUser = BackOfficeUser.getBOUserInstance();
        BOUser.initBOUser(emailField.getText(), passwordField.getText());
        FormOpener.openNewFormAndCloseOld("/return.fxml", "Return Issuer", false, event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
