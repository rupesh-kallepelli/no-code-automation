package com.vr.actions.v1.element.actions.screenshot;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.focus.DOMFocus;
import com.vr.actions.v1.element.actions.screenshot.exception.FailedToTakeException;
import com.vr.cdp.actions.v1.element.actions.focus.Focus;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.page.PageCaptureScreenshot;

public interface Screenshot extends com.vr.cdp.actions.v1.element.actions.screenshot.Screenshot, DOMFocus {
    default String screenshot(Element.Node node, CDPClient client) {
        try {
            Focus focus = focus(node, client);
            PageCaptureScreenshot.Result result = client.sendAndWait(new PageCaptureScreenshot(
                    "png",
                    focus.x(),
                    focus.y(),
                    focus.width(),
                    focus.height()
            ));
            return result.data();
        } catch (Exception e) {
            throw new FailedToTakeException("Failed to take screenshot", e);
        }
    }
}
