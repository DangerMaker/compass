package com.ez08.compass.entity;

/**
 * Created by Administrator on 2016/9/19.
 */

public class ChartsHolderEntity {
    private String name;
    private String code;
    private String person;
    private long number;
    private String position;
    private String time;
    private String reason;
    private int type;   //0:无；1：增持标题；2：减持标题;
    private int end;    //0:无下划线1:有下划线
    //股票名称”,”股票代码”,”高管姓名”,”数量”,”职位”,”操作日期”,”变动原因”

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
