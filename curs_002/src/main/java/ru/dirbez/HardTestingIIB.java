package ru.dirbez;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HardTestingIIB  extends Application implements EventHandler<ActionEvent> {

    private static final String ORDER_BUTTON = "заявка";
    private static final String CALL_BUTTON = "пятиминутный перерыв";
    private static final String DIRBEZ = "Книга менеджера по продажам www.dirbez.ru";

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
                        this.createButtons(ORDER_BUTTON, this),
                        this.createButtons(CALL_BUTTON, this)
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
        if (ORDER_BUTTON.equals(((Button)event.getTarget()).getText())) {
            this.createTaskWindow();
        } else if (CALL_BUTTON.equals(((Button)event.getTarget()).getText())) {
            new TicTacToe().createGameWindow();
        }
    }

    private void createTaskWindow() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene secondScene = new Scene(grid, 500, 500);

        Text scenetitle = new Text("Заполни:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("название задачи");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Описание");
        grid.add(pw, 0, 2);

        TextField pwBox = new TextField();
        grid.add(pwBox, 1, 2);

        Label nc = new Label("Имя клиента");
        grid.add(nc, 0, 3);

        TextField ncBox = new TextField();
        grid.add(ncBox, 1, 3);

        Label pc = new Label("телефон клиента");
        grid.add(pc, 0, 4);

        TextField pcBox = new TextField();
        grid.add(pcBox, 1, 4);

        Label ec = new Label("email клиента");
        grid.add(ec, 0, 5);

        TextField ecBox = new TextField();
        grid.add(ecBox, 1, 5);




        Stage newWindow = new Stage();
        newWindow.setTitle("Добавить задачу");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(200);
        newWindow.setY(100);

        newWindow.show();
        EventHandler<ActionEvent> event = new  EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Logic.getInstance().add(new Task(userTextField.getText(),
                        new Client(null, ncBox.getText(), pcBox.getText(), ecBox.getText()),
                        pwBox.getText())
                );
                newWindow.close();
            }
        };
        grid.add(createButtons("Добавить задачу", event), 1, 6);
        // New window (Stage)
    }
}
