package com.emrekentli.adoptme.model.response;


public class ApiResponse<T>{

    private T data;
    private MetaResponse meta;

    public ApiResponse(T data, MetaResponse meta) {
        this.data = data;
        this.meta = meta;
    }

    public ApiResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("data: ");
        sb.append(data != null ? data.toString() : "null");
        sb.append(", meta: ");
        sb.append(meta != null ? meta.toString() : "null");
        return sb.toString();
    }

}
