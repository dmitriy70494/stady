package ru.dirbez;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableBuilder {
    public TableView<Hotel> createTable() {
        ObservableList<Hotel> hotels = FXCollections.observableArrayList(
                new Hotel("Amara Dolce Vita","Кемер","HV1",4.5),
                new Hotel("Club Boran Mare Beach","Кемер","HV1",4.7),
                new Hotel("Delphin Botanik World of Paradise","Алания","5*",4.4),
                new Hotel("Kamelya World Hotel Fulya","Сиде","5*",4.8),
                new Hotel("Delphin Deluxe Resort","Алания","5*",4.7));
        TableView<Hotel> table = new TableView<Hotel>();
        table.setLayoutX(10);
        table.setLayoutY(50);
        table.setTableMenuButtonVisible(true);
        table.setCursor(Cursor.TEXT);
        table.setTooltip(new Tooltip("Популярные отели Турции"));
        table.setStyle("-fx-font: 14pt Arial;");
        table.setPrefWidth(500);
        table.setPrefHeight(200);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableColumn nameCol = new TableColumn("Отель");
        nameCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("name"));
        nameCol.setPrefWidth(250);
        nameCol.setResizable(false);
        nameCol.setSortable(true);
        TableColumn resortCol = new TableColumn("Курорт");
        resortCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("resort"));
        TableColumn categoryCol = new TableColumn("Категория");
        categoryCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("category"));
        TableColumn rateCol = new TableColumn("Рейтинг");
        rateCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("rate"));
        table.setItems(hotels);
        table.getColumns().addAll(nameCol, resortCol, categoryCol, rateCol);
        return table;
    }

    public class Hotel {
        private String name;

        private String resort;

        private String category;

        private Double rate;

        public Hotel(String name, String resort, String category, Double rate) {
            this.name = name;
            this.resort = resort;
            this.category = category;
            this.rate = rate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResort() {
            return resort;
        }

        public void setResort(String resort) {
            this.resort = resort;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }
    }
}
