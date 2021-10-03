package controllers;

import backoffice_credentials.Package;
import enums.ReturnReason;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import services.ReturnIssueService;
import utils.FormOpener;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {


    @FXML
    private TextField packageUrl;
    @FXML
    private ToggleGroup returnReasons;


    public void onReturn(ActionEvent event) {

        String url = packageUrl.getText(); // HAS TO BE BEFORE detectReason() because the latter calls clearFields and the url-""
        ReturnReason returnReason = detectReason();
        Package returnPackage = new Package(url, returnReason);
        boolean successfulReturn = ReturnIssueService.issueReturn(returnPackage);

        if(successfulReturn == true)
            ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
        else {
            ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
            FormOpener.openAlert();
        }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
