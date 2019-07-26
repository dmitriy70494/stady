package ru.dirbez;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCombination;

import java.util.ArrayList;
import java.util.Map;

public class MenuBuilder {

    private static final String MENU_NAME_FILE = "Файл";
    private static final String MENU_ITEM_NAME_CLIENTS_LIST = "Сформировать список клиентов";
    private static final String MENU_NAME_REPORT = "Отчет";
    private static final String MENU_ITEM_NAME_GRAFIC_SALES_REPORT = "Построить график роста продаж";
    private static final String MENU_ITEM_NAME_GRAFIC_ORDER_REPORT = "Построить график роста заявок";
    private static final String MENU_NAME_REQUEST = "Справка";
    private static final String MENU_ITEM_NAME_ABOUT_PROGRAMM = "О программе";

    public MenuBar createMenu() {
        EventHandler<ActionEvent> handler = new CreateFile();
        MenuBar menuBar = this.createMenuBar();
        Menu file = new Menu(MENU_NAME_FILE);
        file.getItems().addAll(this.createMenuItem(MENU_ITEM_NAME_CLIENTS_LIST, handler));
        Menu report = new Menu(MENU_NAME_REPORT);
        report.getItems().addAll(
                this.createMenuItem(MENU_ITEM_NAME_GRAFIC_SALES_REPORT, handler),
                this.createMenuItem(MENU_ITEM_NAME_GRAFIC_ORDER_REPORT, handler)
        );
        Menu request = new Menu(MENU_NAME_REQUEST);
        request.getItems().addAll(this.createMenuItem(MENU_ITEM_NAME_ABOUT_PROGRAMM, handler));
        menuBar.getMenus().addAll(file, report, request);
        return menuBar;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setLayoutX(10);
        menuBar.setLayoutY(10);
        menuBar.setBlendMode(BlendMode.HARD_LIGHT);
        menuBar.setCursor(Cursor.CLOSED_HAND);
        DropShadow effect=new DropShadow();
        effect.setOffsetX(5);
        effect.setOffsetY(5);
        menuBar.setEffect(effect);
        menuBar.setStyle("-fx-base:skyblue;-fx-border-width:4pt;-fxborder-color:navy;-fx-font:bold 16pt Georgia;");
        menuBar.setPrefSize(250, 50);
        return menuBar;
    }

//    private MenuBar createMenu() {
//        MenuBar menuBar = this.createMenuBar();
//        menuBar.setPrefSize(400, 70);
//        Menu menuF = new Menu("Файл");
//        MenuItem menuItemP = new MenuItem("Печать");
//        menuItemP.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        menuItemP.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
//        menuItemP.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent e) {
//                System.out.println("Идет печать...");
//            } });
//        SeparatorMenuItem sep=new SeparatorMenuItem();
//        RadioMenuItem radioItemY = new RadioMenuItem("С номерами страниц");
//        radioItemY.setStyle("-fx-text-fill:navy;-fx-font:bold italic 12pt Georgia;");
//        ToggleGroup tgroup=new ToggleGroup();
//        radioItemY.setToggleGroup(tgroup);
//        radioItemY.setSelected(true);
//        RadioMenuItem radioItemN = new RadioMenuItem("Без номеров страниц");
//        radioItemN.setStyle("-fx-text-fill:navy;-fx-font:bold italic 12pt Georgia;");
//        radioItemN.setToggleGroup(tgroup);
//        CheckMenuItem checkMenuItem=new CheckMenuItem("Улучшенное качество");
//        checkMenuItem.setSelected(true);
//        checkMenuItem.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        menuF.getItems().addAll(menuItemP, radioItemY, radioItemN, sep,
//                checkMenuItem);
//        Menu menuE = new Menu("Правка");
//        MenuItem menuItemCut = new MenuItem("Вырезать");
//        menuItemCut.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        MenuItem menuItemCopy = new MenuItem("Копировать");
//        menuItemCopy.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        MenuItem menuItemPaste = new MenuItem("Вставить");
//        menuItemPaste.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        menuE.getItems().addAll(menuItemCut, menuItemCopy, menuItemPaste);
//        Menu menuV = new Menu("Вид");
//        MenuItem menuItemS = new MenuItem("Масштаб");
//        menuItemS.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//        menuV.getItems().addAll(menuItemS);
//        menuBar.getMenus().addAll(menuF, menuE, menuV);
//        return menuBar;
//    }

//    private Menu createMenuWithItems(String name, Map<String, EventHandler<ActionEvent>> events) {
//        Menu menu = new Menu(name);
//        for (Map.Entry<String, EventHandler<ActionEvent>> entry : events.entrySet()) {
//            MenuItem menuItem = new MenuItem(entry.getKey());
//            menuItem.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
//            menuItem.setOnAction(entry.getValue());
//            menu.getItems().add(menuItem);
//        }
//        return menu;
//    }

    private MenuItem createMenuItem(String name, EventHandler<ActionEvent> event) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setStyle("-fx-text-fill:navy;-fx-font:bold italic 14pt Georgia;");
        menuItem.setOnAction(event);
        return menuItem;
    }

    private class CreateFile implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            System.out.println(((MenuItem)event.getTarget()).getText());
        }
    }
}
