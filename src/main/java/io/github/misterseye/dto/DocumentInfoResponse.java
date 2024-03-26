package io.github.misterseye.dto;

public class DocumentInfoResponse {
    private String fileName;
    private String contentType;
    private String fileAccessUri;
    private String typeDocument;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileAccessUri() {
        return fileAccessUri;
    }

    public void setFileAccessUri(String fileAccessUri) {
        this.fileAccessUri = fileAccessUri;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }
}
