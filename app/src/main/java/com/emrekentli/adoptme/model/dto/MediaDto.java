package com.emrekentli.adoptme.model.dto;



public class MediaDto {
    private String base64Type;
    private String filePath;
    private String fileName;

    public MediaDto(String base64Type, String filePath, String fileName) {
        this.base64Type = base64Type;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public MediaDto() {
    }

    public String getBase64Type() {
        return base64Type;
    }

    public void setBase64Type(String base64Type) {
        this.base64Type = base64Type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
