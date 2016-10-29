/**
 * 增氧机实体
 */
package com.qican.ygj.bean;

public class Pump {

    public static final String PUMP_ON = "开";
    public static final String PUMP_OFF = "关";

    private String id;
    private String name;
    private String runningState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRunningState() {
        return runningState;
    }

    public void setRunningState(String runningState) {
        this.runningState = runningState;
    }
}
