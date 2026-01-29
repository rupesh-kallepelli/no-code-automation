package com.vr.actions.v1.element.finder;

import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.page.PageGetFrameTree;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractElementFiner implements ElementFinder {
    protected final CDPClient client;
    protected int rootNode = -1;

    protected AbstractElementFiner(CDPClient client) {
        this.client = client;
    }

    protected int getRootNode() throws Exception {
        this.client.sendAndWait(new DOMEnable());
        return this.client.sendAndWait(new DOMGetDocument(-1)).root().nodeId();
    }

    protected int getRootNode(String frameId) throws Exception {
        this.client.sendAndWait(new DOMEnable());
        return this.client.sendAndWait(new DOMGetDocument(frameId, -1)).root().nodeId();
    }

    protected List<PageGetFrameTree.Frame> getFlattenedFrames() throws Exception {
        PageGetFrameTree.Result treeResult = this.client.sendAndWait(new PageGetFrameTree());
        return FrameUtils.flattenFrames(treeResult.frameTree());
    }

    protected static final class FrameUtils {

        private FrameUtils() {
        }

        public static List<PageGetFrameTree.Frame> flattenFrames(
                PageGetFrameTree.FrameTree root
        ) {
            List<PageGetFrameTree.Frame> frames = new ArrayList<>();
            collect(root, frames);
            return frames;
        }

        private static void collect(
                PageGetFrameTree.FrameTree node,
                List<PageGetFrameTree.Frame> out
        ) {
            if (node == null) return;

            // add current frame
            if (node.frame() != null) {
                out.add(node.frame());
            }

            // recurse into children
            if (node.childFrames() != null) {
                for (PageGetFrameTree.FrameTree child : node.childFrames()) {
                    collect(child, out);
                }
            }
        }
    }


}
