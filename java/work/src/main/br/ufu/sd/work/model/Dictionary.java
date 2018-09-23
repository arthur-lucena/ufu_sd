package br.ufu.sd.work.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ismaley on 19/09/18.
 */
public class Dictionary {

    private ConcurrentHashMap<Long, byte[]> data;

    public Dictionary(ConcurrentHashMap<Long, byte[]> data) {
        this.data = data;
    }

    public ConcurrentHashMap<Long, byte[]> getData() {
        return data;
    }

    public void setData(ConcurrentHashMap<Long, byte[]> data) {
        this.data = data;
    }
}
