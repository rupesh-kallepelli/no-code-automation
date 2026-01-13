import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.ws.RawCDPClient;
import com.vr.cdp.protocol.command.page.PageEnable;
import com.vr.cdp.protocol.command.page.PageNavigate;

public class ClientSmokeTest {

    public static void main(String[] args) throws Exception {

        String wsUrl =
                "ws://localhost:1000/devtools/page/7EA67EA95D101011F06DC64C7B2AF152";

        CDPClient client = new RawCDPClient(wsUrl);

        client.send(new PageEnable());

        PageNavigate.Result r =
                client.sendAndWait(
                        new PageNavigate("https://example.com")
                );

        System.out.println("Navigated, frameId=" + r.frameId());

        client.close();
    }
}
