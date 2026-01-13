package com.vr.actions.page;

import com.vr.actions.ActionContext;
import com.vr.actions.uitl.ScreenshotUtil;
import com.vr.cdp.protocol.command.page.*;

import java.util.Base64;

public class PageActions {

    private final ActionContext ctx;

    public PageActions(ActionContext ctx) {
        this.ctx = ctx;
    }

    public void enable() throws Exception {
        ctx.client().send(new PageEnable());
    }

    public void disable() throws Exception {
        ctx.client().send(new PageDisable());
    }

    public String navigate(String url) throws Exception {
        PageNavigate.Result r =
                ctx.client().sendAndWait(new PageNavigate(url));
        return r.frameId();
    }

    public void reload() throws Exception {
        ctx.client().sendAndWait(new PageReload(true));
    }

    public byte[] screenshotPng() throws Exception {
        PageCaptureScreenshot.Result r =
                ctx.client().sendAndWait(new PageCaptureScreenshot("png"));
        return Base64.getDecoder().decode(r.data());
    }

    public void screenshot(String file) throws Exception {
        var result =
                ctx.client().sendAndWait(new PageCaptureScreenshot("png"));
        ScreenshotUtil.saveBase64Image(result.data(), file);
    }

}
