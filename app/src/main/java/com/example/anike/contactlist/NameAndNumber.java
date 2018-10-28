package com.example.anike.contactlist;

/**
 * Created by anike on 22-10-2018.
 */

public class NameAndNumber {
    private String Name;
    private String Number;
    private Boolean checkBox;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public NameAndNumber(String name, String number) {
        this.Name = name;
        this.Number = number;
        this.checkBox = false;

    }
}
