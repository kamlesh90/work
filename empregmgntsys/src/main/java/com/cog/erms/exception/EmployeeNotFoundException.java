package com.cog.erms.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public String message;
    public EmployeeNotFoundException(){

    }
    public EmployeeNotFoundException(String msg){
        this.message=msg;
    }
}
