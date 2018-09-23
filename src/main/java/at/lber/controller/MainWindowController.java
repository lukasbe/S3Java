package at.lber.controller;

import at.lber.util.NoConnectionException;
import at.lber.util.S3Connection;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resources;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MainWindowController {

    private static final Logger LOG = LoggerFactory.getLogger(MainWindowController.class);

    @FXML ListView<String> lv_buckets;
    @FXML ListView<String> lv_files;
    @FXML MenuItem newConItem;

    private AmazonS3 s3Client;

    @FXML
    public void initialize() {

        ObservableList<String> files = FXCollections.observableArrayList("File 1", "File 2", "File 3");
        lv_files.setItems(files);

        newConItem.setOnAction(event -> enterCredentials());

    }

    private void enterCredentials(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Enter S3 Credentials");


        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        //Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Acces Key");
        PasswordField password = new PasswordField();
        password.setPromptText("Secret Key");

        grid.add(new Label("Access Key"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Secret Key"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(in -> S3Connection.setCredentials(in.getKey(), in.getValue()));
        try {
            s3Client = S3Connection.getConnection();
            updateBuckets();
        } catch (NoConnectionException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("No valid S3 connection entered!");
            alert.setContentText("Please setup a valid S3 connection!");

            alert.showAndWait();
        }
    }

    private void updateBuckets(){
            List<Bucket> bucketSrc = s3Client.listBuckets();
            List<String> bucketNames = bucketSrc.stream().map(b -> b.getName()).collect(Collectors.toList());

            ObservableList<String> buckets = FXCollections.observableArrayList(bucketNames);
            lv_buckets.setItems(buckets);
    }

}
