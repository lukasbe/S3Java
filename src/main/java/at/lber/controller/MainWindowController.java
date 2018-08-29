package at.lber.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resources;
import java.util.Observable;

public class MainWindowController {

    private static final Logger LOG = LoggerFactory.getLogger(MainWindowController.class);

    @FXML ListView<String> lv_buckets;
    @FXML ListView<String> lv_files;

    @FXML
    public void initialize() {
        ObservableList<String> buckets = FXCollections.observableArrayList("Bucket 1", "Bucket 2", "Bucket 3");
        lv_buckets.setItems(buckets);

        ObservableList<String> files = FXCollections.observableArrayList("File 1", "File 2", "File 3");
        lv_files.setItems(files);
    }

}
