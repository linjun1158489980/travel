package cn.itcast.travel.domain;

import java.io.Serializable;
import java.util.List;

public class FavoritCount<T> implements Serializable {
    private  int rid;
    private  int number;
    private List<T> list;
    public FavoritCount(int rid, int number) {
        this.rid = rid;
        this.number = number;
    }

    public FavoritCount() {
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "FavoritCount{" +
                "rid=" + rid +
                ", number=" + number +
                '}';
    }
}
