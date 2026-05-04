package edu.cit.baldon.foodieorderingapp.shared;

public class ApiError {
    private String code;
    private String type;
    private String detail;

    public ApiError() {}

    public ApiError(String code, String type, String detail) {
        this.code = code;
        this.type = type;
        this.detail = detail;
    }

    public String getCode()   { return code; }
    public void setCode(String code) { this.code = code; }

    public String getType()   { return type; }
    public void setType(String type) { this.type = type; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
