package com.vr.cdp.protocol.command.runtime;

public record ExecutionContextCreatedEvent(
        String method,
        Params params
) {
    public record Params(
            ExecutionContextDescription context
    ) {
    }

    public record ExecutionContextDescription(
            int id,
            String origin,
            String name,
            String uniqueId,
            AuxData auxData
    ) {
    }

    public record AuxData(
            String frameId,
            Boolean isDefault,
            String type
    ) {
    }
}


