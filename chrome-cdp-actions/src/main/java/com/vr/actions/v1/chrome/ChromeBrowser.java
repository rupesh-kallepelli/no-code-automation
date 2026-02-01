package com.vr.actions.v1.chrome;

import com.vr.actions.v1.chrome.exception.BrowserCreationException;
import com.vr.actions.v1.client.PageCDPClient;
import com.vr.actions.v1.client.ScreenCastClient;
import com.vr.actions.v1.page.chromium.ChromePage;
import com.vr.actions.v1.page.chromium.exception.BroadCasterCannotBeNull;
import com.vr.actions.v1.page.chromium.exception.PageClosingException;
import com.vr.actions.v1.page.chromium.exception.PageCreationException;
import com.vr.cdp.actions.v1.browser.Browser;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.cdp.protocol.command.target.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class ChromeBrowser implements Browser {
    private final PageCDPClient cdpClient;
    private final String sessionId;

    public ChromeBrowser(String sessionId, String browserWsUrl) {
        try {
            this.sessionId = sessionId;
            this.cdpClient = new PageCDPClient(browserWsUrl);
        } catch (Exception e) {
            throw new PageCreationException("Exception while creating page", e);
        }
    }

    public ChromeBrowser(
            String sessionId,
            String browserWsUrl,
            boolean enableCasting,
            BroadCaster broadCaster
    ) {
        try {
            this.sessionId = sessionId;
            if (enableCasting) {
                if (Objects.isNull(broadCaster)) throw new BroadCasterCannotBeNull("Broadcaster is null");
                this.cdpClient = new ScreenCastClient(browserWsUrl, broadCaster);
            } else
                this.cdpClient = new PageCDPClient(browserWsUrl);

            cdpClient.sendAndWait(new TargetSetDiscoverTargets(true));

        } catch (URISyntaxException | InterruptedException e) {
            throw new BrowserCreationException("Unable to creation browser session", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page getPage() {
        try {

            TargetCreateTarget.Result newTarget = cdpClient.sendAndWait(
                    new TargetCreateTarget("about:blank")
            );

            TargetAttachToTarget.Result attachedNew = cdpClient.sendAndWait(
                    new TargetAttachToTarget(newTarget.targetId(), true)
            );

            Page page = new ChromePage(
                    attachedNew.sessionId(),
                    cdpClient
            );

            cdpClient.setPage(page);
            return page;

        } catch (Exception e) {
            throw new PageCreationException("Exception during creating page", e);
        }
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }


    @Override
    public Page createPage(String url) {
        try {

            TargetCreateTarget.Result result = cdpClient.sendAndWait(new TargetCreateTarget(url));

            return new ChromePage(
                    result.targetId(),
                    cdpClient
            );
        } catch (Exception e) {
            throw new PageCreationException("Exception during creating page", e);
        }
    }

    @Override
    public Page switchToPage(String id) {
        return new ChromePage(
                id,
                cdpClient
        );
    }

    @Override
    public boolean deletePage(String id) {
        try {

            TargetCloseTarget.Result result = cdpClient.sendAndWait(new TargetCloseTarget(id));

            return result.success();
        } catch (Exception e) {
            throw new PageClosingException("Exception while closing the page", e);
        }
    }

    @Override
    public List<Page> getPages() {
        try {

            TargetGetTargets.Result targets = cdpClient.sendAndWait(new TargetGetTargets());
            return targets.targetInfos().stream()
                    .map(targetInfo -> (Page) new ChromePage(
                            targetInfo.targetId(),
                            cdpClient)
                    )
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        cdpClient.close();
    }
}
