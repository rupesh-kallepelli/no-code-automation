package com.vr.cdp.runtime.page;

import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.page.PageCaptureScreenshot;
import com.vr.cdp.protocol.command.page.PageEnable;
import com.vr.cdp.protocol.command.page.PageNavigate;
import com.vr.cdp.protocol.command.page.PageReload;
import com.vr.cdp.runtime.dom.DOMService;
import com.vr.cdp.runtime.util.ScreenshotUtil;
import com.vr.cdp.runtime.wait.PageLoadWaiter;

public class Page {

    private final CDPClient client;
    private final DOMService dom;

    public Page(CDPClient client) {
        this.client = client;
        this.dom = new DOMService(client);
    }

    public void enable() throws Exception {
        client.send(new PageEnable());
    }

    public void navigate(String url) throws Exception {
        PageLoadWaiter waiter = new PageLoadWaiter(client);
        client.sendAndWait(new PageNavigate(url));
        waiter.await(10_000);
    }

    public void reload() throws Exception {
        client.sendAndWait(new PageReload(true));
    }

    public void screenshot(String file) throws Exception {
        var result =
                client.sendAndWait(new PageCaptureScreenshot("png"));
        ScreenshotUtil.saveBase64Image(result.data(), file);
    }

    public DOMService dom() {
        return dom;
    }
}
