package com.vr.actions.v1.element.actions.focus;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.focus.exception.UnableToFocusException;
import com.vr.actions.v1.element.actions.scroll.RuntimeScrollIntoView;
import com.vr.cdp.actions.v1.element.actions.focus.Focus;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeCallFunctionOn;

import java.util.List;
import java.util.Map;

public interface RuntimeFocus {
    default Focus focus(Element.Node node, CDPClient client) {
        try {
            RuntimeScrollIntoView.scrollIntoView(client, node);
            RuntimeCallFunctionOn.Result result = client.sendAndWait(new RuntimeCallFunctionOn(
                    """
                            function() {
                                this.scrollIntoView({ block: 'center', inline: 'center' });
                                this.focus(); const r = this.getBoundingClientRect();
                                return {
                                    centerX: r.left + r.width / 2,
                                    centerY: r.top + r.height / 2,
                                    width: r.width,
                                    height: r.height,
                                    left: r.left,
                                    top: r.top,
                                };
                            }"""
                    , node.getObjectId(),
                    List.of()
            ));
            Map<String, Object> value = (Map<String, Object>) result.result().value();
            return new Focus((Number) value.get("centerX"),
                    (Number) value.get("centerY"),
                    (Number) value.get("left"),
                    (Number) value.get("centerX"),
                    (Number) value.get("top"),
                    (Number) value.get("width")
            );
        } catch (Exception e) {
            throw new UnableToFocusException("Unable to focus", e);
        }
    }
}
