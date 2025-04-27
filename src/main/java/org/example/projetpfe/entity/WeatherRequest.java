package org.example.projetpfe.entity;

import lombok.Data;

@Data
public class WeatherRequest {
    private String dataset;
    private String type;
    private String step;
    private String param;
    private String levtype;
    private String format;
    private String time;
    private String area;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getLevtype() {
        return levtype;
    }

    public void setLevtype(String levtype) {
        this.levtype = levtype;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

