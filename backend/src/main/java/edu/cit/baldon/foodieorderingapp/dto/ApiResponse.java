package edu.cit.baldon.foodieorderingapp.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ApiError error;
    private String timestamp;
    private Integer status; // Used in some endpoints (e.g. Register)

    public ApiResponse() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public ApiError getError() { return error; }
    public void setError(ApiError error) { this.error = error; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
