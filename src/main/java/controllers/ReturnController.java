package controllers;

import backoffice_credentials.Package;
import enums.ReturnReason;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import org.openqa.selenium.NoSuchElementException;
import services.ReturnIssueService;
import utils.FormOpener;
import utils.FormValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {


    @FXML
    private TextField packageUrl;
    @FXML
    private ToggleGroup returnReasons;
    @FXML
    private Label urlErrorLabel;
    @FXML
    private Label reasonErrorLabel;


    public void onReturn(ActionEvent event) {

        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
        urlErrorLabel.setText("");
        reasonErrorLabel.setText("");

        String url = packageUrl.getText(); // HAS TO BE BEFORE detectReason() because the latter calls clearFields and the url-""
        if(validateFields() == true) {
            ReturnReason returnReason = detectReason();
            Package returnPackage = new Package(url, returnReason);

            boolean successfulReturn = false;
            try {
                successfulReturn = ReturnIssueService.issueReturn(returnPackage);
            }
            catch (NoSuchElementException e) {
                FormOpener.openAlert("Failed return", "There is no option to return this package!");
            }
        }
    }

    private boolean validateFields() {

        String url = packageUrl.getText();
        boolean urlFormat = FormValidator.urlValidator(url);
        boolean reasonSelected;
        if(returnReasons.getSelectedToggle() == null)
            reasonSelected = false;
        else
            reasonSelected = true;

        if(urlFormat == true && reasonSelected == true && url.contains("backoffice") == true)
            return true;
        if(urlFormat == false || url.contains("backoffice") == false)
            urlErrorLabel.setText("Invalid URL!");
        if(reasonSelected == false)
            reasonErrorLabel.setText("Please select a reason!");
        return false;


    }

    private ReturnReason detectReason() {

        RadioButton selectedReason = (RadioButton) returnReasons.getSelectedToggle();
        String reasonID = selectedReason.getId();
        ReturnReason returnReason = ReturnReason.valueOf(reasonID);

        clearSelected(selectedReason);
        return returnReason;
    }

    private void clearSelected(RadioButton selectedReason) {

        packageUrl.clear();
        selectedReason.setSelected(false);
    }

    public void onEditLogin(ActionEvent event) throws IOException {

        FormOpener.openNewFormAndCloseOld("/login_page.fxml", "Log In", false, event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
