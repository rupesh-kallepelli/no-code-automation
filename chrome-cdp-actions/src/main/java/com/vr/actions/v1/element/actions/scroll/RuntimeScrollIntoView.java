package com.vr.actions.v1.element.actions.scroll;

import com.vr.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeCallFunctionOn;

import java.util.List;

public interface RuntimeScrollIntoView {
    static void scrollIntoView(CDPClient client, Element.Node node) throws Exception {
        client.sendAndWait(new RuntimeCallFunctionOn(
                """
                        function() {
                            this.scrollIntoView(
                                {
                                    block: 'center',
                                    inline: 'center'
                                }
                            );
                        }""",
                node.getObjectId(),
                List.of()
        ));
    }
}
