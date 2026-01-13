package com.vr.cdp.protocol.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class CDPCommand<R> {

    private int id;
    private final String method;

    protected CDPCommand(String method) {
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public abstract Object getParams();

    @JsonIgnore
    public abstract Class<R> getResultType();
}
