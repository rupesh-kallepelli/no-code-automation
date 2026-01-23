package com.vr.cdp.protocol.command.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class DOMGetBoxModel
        extends DOMCommand<DOMGetBoxModel.Result> {

    private final Params params;

    public DOMGetBoxModel(int nodeId) {
        super("DOM.getBoxModel");
        this.params = new Params(nodeId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(int nodeId) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(BoxModel model) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BoxModel(
            List<Double> content,
            List<Double> padding,
            List<Double> border,
            List<Double> margin,
            Number width,
            Number height
    ) {}
}
