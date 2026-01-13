package com.vr.cdp.protocol.command.input;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.List;

public class InputDispatchTouchEvent extends InputCommand<EmptyResult> {

    private final Params params;

    public InputDispatchTouchEvent(String type, List<TouchPoint> points) {
        super("Input.dispatchTouchEvent");
        this.params = new Params(type, points);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(
            String type,              // touchStart, touchMove, touchEnd
            List<TouchPoint> touchPoints
    ) {}

    public record TouchPoint(
            double x,
            double y,
            double radiusX,
            double radiusY,
            double force
    ) {}
}
