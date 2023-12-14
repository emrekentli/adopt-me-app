package com.emrekentli.adoptme.model.response;


public class PageResponse<T> {
    private Page<T> items =  new Page();

    public PageResponse(Page<T> items) {
        this.items = items;
    }

    public PageResponse() {
    }

    public Page<T> getItems() {
        return items;
    }

    public void setItems(Page<T> items) {
        this.items = items;
    }

}
