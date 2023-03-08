package dev.claybradley.industrialautomationtoolkit.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
@Component
@Scope("prototype")
public class MainController implements Initializable {
    @Autowired
    ApplicationContext applicationContext;
    @FXML
    private VBox bodyvbox;
    @FXML
    private BorderPane rootbp;
    @FXML
    private VBox sidenavvbox;

    public MainController(){
    }
    @FXML
    private void navhome(ActionEvent actionEvent) {
        loadPage("home");
    }
    @FXML
    private void navmodbus(ActionEvent actionEvent) {
        loadPage("/modbus/ModbusMain");
    }
    @FXML
    private void navpage02(ActionEvent actionEvent) {
        loadPage("page02-view");
    }
    @FXML
    private void navpage03(ActionEvent actionEvent) {
        loadPage("page03-view");
    }

    public void loadPage(String page){
        Parent root = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + page + ".fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bodyvbox.getChildren().clear();
        VBox.setVgrow(root, Priority.ALWAYS);
        bodyvbox.getChildren().add(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPage("home");
    }
}
