package com.vr.actions.page.chromium;

import com.vr.actions.chrome.ChromeInstance;
import com.vr.actions.dom.DOMActions;
import com.vr.actions.input.InputActions;
import com.vr.actions.page.Page;
import com.vr.actions.uitl.ScreenshotUtil;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.cdp.client.ws.RawCDPClient;
import com.vr.cdp.client.ws.ScreenCastClient;
import com.vr.cdp.protocol.command.page.*;
import java.io.File;
import java.util.Base64;

public class ChromiumPage extends ChromeInstance implements Page {
    private final CDPClient client;
    private final DOMActions dom;
    private final InputActions input;
    private BroadCaster broadCaster;

    public ChromiumPage(
            Process process,
            String browserWs,
            String pageWs,
            File userDataDir,
            boolean enableBroadCasting,
            BroadCaster broadCaster
    ) throws Exception {
        super(process, browserWs, pageWs, userDataDir);
        if (enableBroadCasting) {
            client = new ScreenCastClient(pageWs, broadCaster);
        } else
            client = new RawCDPClient(pageWs);
        this.dom = new DOMActions(client);
        this.input = new InputActions(client);
    }

    @Override
    public void enable() throws Exception {
        client.send(new PageEnable());
    }

    @Override
    public void disable() throws Exception {
        client.send(new PageDisable());
    }

    @Override
    public String navigate(String url) throws Exception {
        PageNavigate.Result r =
                client.sendAndWait(new PageNavigate(url));
        return r.frameId();
    }

    @Override
    public void reload() throws Exception {
        client.sendAndWait(new PageReload(true));
    }

    @Override
    public byte[] screenshotPng() throws Exception {
        PageCaptureScreenshot.Result r =
                client.sendAndWait(new PageCaptureScreenshot("png"));
        return Base64.getDecoder().decode(r.data());
    }

    @Override
    public void screenshot(String file) throws Exception {
        var result =
                client.sendAndWait(new PageCaptureScreenshot("png"));
        ScreenshotUtil.saveBase64Image(result.data(), file);
    }

    @Override
    public void click(String selector) throws Exception {
        int nodeId = dom.find(selector);
        input.click(nodeId);
    }

    @Override
    public void type(String selector, String text) throws Exception {
        int nodeId = dom.find(selector);
        dom.focus(nodeId);
        input.type(text);
    }

    @Override
    public void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) throws Exception {
        client.send(new PageStartScreencast(format, quality, maxWidth, maxHeight));
    }
}
