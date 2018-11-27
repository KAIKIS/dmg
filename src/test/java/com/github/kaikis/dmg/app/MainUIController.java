package com.github.kaikis.dmg.app;

import com.github.kaikis.dmg.Data;
import com.github.kaikis.dmg.MqttClientVerticle;
import io.vertx.core.Vertx;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.Date;


public class MainUIController {

    @FXML
    private TableView<DataProperty> dataTable;

    @FXML
    private TableColumn<DataProperty, Number> deviceIdCol;

    @FXML
    private TableColumn<DataProperty, Number> nodeIdCol;

    @FXML
    private TableColumn<DataProperty, Number> dataIdCol;

    @FXML
    private TableColumn<DataProperty, String> dataTypeCol;

    @FXML
    private TableColumn<DataProperty, String> dataCol;

    @FXML
    private TableColumn<DataProperty, Date> createTimeCol;

    ObservableList<DataProperty> lists = FXCollections.observableArrayList();


    public MainUIController() {
    }

    @FXML
    private void initialize() {
        deviceIdCol.setCellValueFactory(cell -> cell.getValue().deviceIdProperty());
        nodeIdCol.setCellValueFactory(cell -> cell.getValue().nodeIdProperty());
        dataIdCol.setCellValueFactory(cell -> cell.getValue().dataIdProperty());
        dataCol.setCellValueFactory(cell -> cell.getValue().dataProperty());
        dataTypeCol.setCellValueFactory(cell -> cell.getValue().dataTypeProperty());
        createTimeCol.setCellValueFactory(cell -> cell.getValue().createTimeProperty());

        Data data = new Data();
        data.setDeviceId(1);
        data.setDataId(1);
        data.setNodeId(1);
        data.setData("123");
        data.setCreateTime(new Date());
        data.setDataType("int");

        dataTable.setItems(lists);

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new WebsocketClientVerticle(lists));
    }
}
