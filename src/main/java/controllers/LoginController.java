package controllers;

import backoffice_credentials.BackOfficeUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.FormOpener;
import utils.FormValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label passwordErrorLabel;


    public void onSave(ActionEvent event) throws IOException {

        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        String email = emailField.getText();
        String password = passwordField.getText();

        if(validateFields(email, password) == true) {
            BackOfficeUser BOUser = BackOfficeUser.getBOUserInstance();
            BOUser.initBOUser(email, password);
            FormOpener.openNewFormAndCloseOld("/return.fxml", "Return Issuer", false, event);
        }
    }

    private boolean validateFields(String email, String password) {

        boolean emailFormat = FormValidator.emailValidator(email);
        boolean passwordFormat = FormValidator.passwordValidator(password);

        if(emailFormat == true && passwordFormat == true)
            return true;

        if(emailFormat == false)
            emailErrorLabel.setText("Incorrect email format!");
        if(passwordFormat == false)
            passwordErrorLabel.setText("The password you entered is too short!");
        return false;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String email = BackOfficeUser.getBOUserInstance().getEmail();
        String password = BackOfficeUser.getBOUserInstance().getPassword();
        if(email != null && password != null) {
            emailField.setText(email);
            passwordField.setText(password);
        }
    }
}
