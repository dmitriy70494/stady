package dirbez;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuBuilder {

    public static final String MENU_NAME_FILE = "Файл";
    public static final String MENU_ITEM_NAME_CLIENTS_LIST = "Сформировать список клиентов";
    public static final String MENU_NAME_REPORT = "Отчет";
    public static final String MENU_ITEM_NAME_GRAFIC_SALES_REPORT = "Построить график роста продаж";
    public static final String MENU_ITEM_NAME_GRAFIC_ORDER_REPORT = "Построить график роста заявок";
    public static final String MENU_NAME_REQUEST = "Справка";
    public static final String MENU_ITEM_NAME_ABOUT_PROGRAMM = "О программе";

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
            if (MenuBuilder.MENU_ITEM_NAME_ABOUT_PROGRAMM.equals(((MenuItem)event.getTarget()).getText())) {
                createWindowAboutProgramm();
            }
        }
    }
    private void createWindowAboutProgramm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene secondScene = new Scene(grid, 370, 200);

        Text scenetitle = new Text("Парсер базы работы");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Версия 1.0");
        grid.add(userName, 0, 1);

        Label pw = new Label("Разработана Баландиным Д.В.");
        grid.add(pw, 0, 2);

        Label nc = new Label("01.2019");
        grid.add(nc, 0, 3);

        Label pc = new Label("г. Екатеринбург");
        grid.add(pc, 0, 4);

        Label ec = new Label("email: d89086362742@yandex.ru");
        grid.add(ec, 0, 5);

        Stage newWindow = new Stage();
        newWindow.setTitle("О программе");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(200);
        newWindow.setY(100);

        newWindow.show();
    }
}
