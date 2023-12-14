package com.emrekentli.adoptme.model.response;


public class MetaResponse {

    private String code;
    private String description;

    public MetaResponse(String code, String description) {
    }

    public static MetaResponse of(String code, String description){
        return new MetaResponse(code, description);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


}
