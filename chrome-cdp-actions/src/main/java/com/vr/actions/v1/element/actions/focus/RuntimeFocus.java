package com.vr.actions.v1.element.actions.focus;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.scroll.RuntimeScrollIntoView;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeCallFunctionOn;

import java.util.List;

public interface RuntimeFocus {
    default Focus focus(Element.Node node, CDPClient client) throws Exception {
        RuntimeScrollIntoView.scrollIntoView(client, node);
        RuntimeCallFunctionOn.Result result = client.sendAndWait(new RuntimeCallFunctionOn(
                """
                        function() {
                            this.scrollIntoView({ block: 'center', inline: 'center' });
                            this.focus(); const r = this.getBoundingClientRect();
                            return {
                                x: r.left + r.width / 2,
                                y: r.top + r.height / 2
                            };
                        }"""
                , node.getObjectId(),
                List.of()
        ));

        return new Focus((Number) result.result().value().get("x"),
                (Number) result.result().value().get("y")
        );
    }
}
