package dirbez;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HardTestingIIB  extends Application implements EventHandler<ActionEvent> {

    private static final String ORDER_BUTTON = "запустить";
    private static final String DIRBEZ = "База работы www.dirbez.ru";

    private void showAlert(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(DIRBEZ);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();
        border.setTop(new MenuBuilder().createMenu());
        border.setLeft(
                this.createVBox(
                        this.createButtons(ORDER_BUTTON, this)
                )
        );
        Client client = new Client();
        client.setId(5);
        border.setCenter(new TableBuilder().createTable(Logic.getInstance().getAll()));
        stage.setScene(new Scene(border, 1000, 700));
        stage.setTitle(DIRBEZ);
        stage.setResizable(false);
        stage.show();
    }

    private Button createButtons(String name, EventHandler<ActionEvent> event) {
        Button button = new Button();
        button.setLayoutX(20);
        button.setLayoutY(20);
        button.setText(name);
        button.setOnAction(event);
        return button;
    }

    private VBox createVBox(Node ... nodes) {
        VBox vbox = new VBox();
        vbox.setPrefHeight(40);
        vbox.setSpacing(10.0);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.getChildren().addAll(nodes);
        return vbox;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println(((Button)event.getTarget()).getText());
        new dirbez.jobstore.StartUI().init();
    }
}
