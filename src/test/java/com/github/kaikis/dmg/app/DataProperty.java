package com.github.kaikis.dmg.app;

import com.github.kaikis.dmg.Data;
import javafx.beans.property.*;

import java.util.Date;

public class DataProperty {
    private IntegerProperty deviceId;

    private IntegerProperty dataId;

    private IntegerProperty nodeId;

    private StringProperty dataType;

    private StringProperty data;

    private ObjectProperty<Date> createTime;

    public DataProperty(Data data) {
        this.deviceId = new SimpleIntegerProperty(data.getDeviceId());
        this.dataId = new SimpleIntegerProperty(data.getDataId());
        this.nodeId = new SimpleIntegerProperty(data.getNodeId());
        this.dataType = new SimpleStringProperty(data.getDataType());
        this.data = new SimpleStringProperty(data.getData());
        this.createTime = new SimpleObjectProperty<Date>(data.getCreateTime());
    }

    public int getDeviceId() {
        return deviceId.get();
    }

    public IntegerProperty deviceIdProperty() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId.set(deviceId);
    }

    public int getDataId() {
        return dataId.get();
    }

    public IntegerProperty dataIdProperty() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId.set(dataId);
    }

    public int getNodeId() {
        return nodeId.get();
    }

    public IntegerProperty nodeIdProperty() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId.set(nodeId);
    }

    public String getDataType() {
        return dataType.get();
    }

    public StringProperty dataTypeProperty() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType.set(dataType);
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }

    public void setData(String data) {
        this.data.set(data);
    }

    public Date getCreateTime() {
        return createTime.get();
    }

    public ObjectProperty<Date> createTimeProperty() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime.set(createTime);
    }
}
