package dirbez;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TableBuilder {
    public TableView<Task> createTable(List<Task> tasks) {
        ObservableList<Task> hotels = FXCollections.observableList(tasks);
        TableView<Task> table = new TableView<>();
        table.setLayoutX(10);
        table.setLayoutY(50);
        table.setTableMenuButtonVisible(true);
        table.setCursor(Cursor.TEXT);
        table.setTooltip(new Tooltip("Работа"));
        table.setStyle("-fx-font: 14pt Arial;");
        table.setPrefWidth(500);
        table.setPrefHeight(200);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableColumn nameCol = new TableColumn("заголовок");
        nameCol.setCellValueFactory(new PropertyValueFactory<Task,String>("name"));
        nameCol.setPrefWidth(250);
        nameCol.setResizable(false);
        nameCol.setSortable(true);
        TableColumn resortCol = new TableColumn("работа");
        resortCol.setCellValueFactory(new PropertyValueFactory<Task, Client>("client"));
        TableColumn categoryCol = new TableColumn("дата");
        categoryCol.setCellValueFactory(new PropertyValueFactory<Task,String>("description"));
        table.setItems(hotels);
        table.getColumns().addAll(nameCol, resortCol, categoryCol);
        return table;
    }
}
