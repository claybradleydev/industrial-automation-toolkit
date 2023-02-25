package dev.claybradley.industrialscanner.home;

import dev.claybradley.industrialscanner.modbus.slave.ModbusSlaveService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SideBarController {


    @FXML
    private VBox bodyvbox;
    @FXML
    private BorderPane rootbp;
    @FXML
    private VBox sidenavvbox;


    @FXML
    private void navhome(ActionEvent actionEvent) {
        rootbp.setCenter(bodyvbox);
    }
    @FXML
    private void navmodsim(ActionEvent actionEvent) {
        loadPage("modsim-view");
    }
    @FXML
    private void navpage02(ActionEvent actionEvent) {
        loadPage("page02-view");
    }
    @FXML
    private void navpage03(ActionEvent actionEvent) {
        loadPage("page03-view");
    }
    private void loadPage(String page){
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/fxml/" + page + ".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        rootbp.setCenter(root);
    }
}
