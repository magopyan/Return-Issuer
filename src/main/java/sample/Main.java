package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.FormOpener;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FormOpener.openNewForm("/login_page.fxml", "Return Issuer | Log In");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
