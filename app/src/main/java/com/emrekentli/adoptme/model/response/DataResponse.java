package com.emrekentli.adoptme.model.response;


import java.util.ArrayList;
import java.util.List;

public class DataResponse<T> {
    private List<T> items = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("listItems: ");
        if(items != null){
            for(T item : items){
                sb.append(item.toString());
            }
        }
        return sb.toString();
    }

    public DataResponse(List<T> items) {
        this.items = items;
    }

    public DataResponse() {
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
