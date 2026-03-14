package com.vr.smartlocator.engine;

import com.vr.smartlocator.engine.model.LocatorCandidate;
import com.vr.smartlocator.engine.model.LocatorType;
import com.vr.smartlocator.engine.resolver.UniqueLocatorResolver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniqueLocatorResolverTest {


    @Test
    void shouldResolveById() {

        String html = """
                    <button id="loginBtn">Login</button>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.getElementById("loginBtn");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertEquals(LocatorType.CSS, c.getLocator().getType());
        assertEquals("#loginBtn", c.getLocator().getValue());
        assertEquals("button", c.getTag());
        assertEquals("Login", c.getText());
        assertEquals(1, c.getIndex());
    }

    @Test
    void shouldResolveByAttribute() {

        String html = """
                    <input name="username" type="text"/>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.selectFirst("input");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(c.getLocator().getValue().contains("name='username'"));
        assertEquals("input", c.getTag());
    }

    @Test
    void shouldResolveByText() {

        String html = """
                    <button>Submit</button>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.selectFirst("button");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(c.getLocator().getValue().contains("Submit"));
        assertEquals("Submit", c.getText());
    }

    @Test
    void shouldResolveByAncestorContext() {

        String html = """
                    <form id="loginForm">
                        <button class="btn primary">Login</button>
                    </form>
                    <header>
                        <button class="btn primary">Login</button>
                    </header>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.select("#loginForm button").first();

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(c.getLocator().getValue().contains("#loginForm"));
        assertEquals("button", c.getTag());
        assertEquals("Login", c.getText());
    }

    @Test
    void shouldResolveByIndexWhenDuplicate() {

        String html = """
                    <button>OK</button>
                    <button>OK</button>
                    <button>OK</button>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.select("button").get(1); // second

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertEquals(LocatorType.XPATH, c.getLocator().getType());
        assertTrue(c.getLocator().getValue().contains("(//button)[2]"));
        assertEquals(2, c.getIndex());
    }

    @Test
    void shouldFallbackToAbsoluteXpath() {

        String html = """
                    <div>
                        <span><a>Click</a></span>
                    </div>
                    <div>
                        <span><a>Click</a></span>
                    </div>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.select("a").get(1); // second

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(
                c.getLocator().getType() == LocatorType.XPATH ||
                        c.getLocator().getType() == LocatorType.ABSOLUTE_XPATH
        );
    }

    @Test
    void shouldBuildCorrectDomPath() {

        String html = """
                    <div>
                      <ul>
                        <li>
                          <button>Go</button>
                        </li>
                      </ul>
                    </div>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.selectFirst("button");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(c.getDomPath().contains("/button[1]"));
    }

    @Test
    void shouldSetCorrectTagAndText() {

        String html = """
                    <button class="primary">Save</button>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.selectFirst("button");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertEquals("button", c.getTag());
        assertEquals("Save", c.getText());
    }

    @Test
    void shouldResolveTableRowButtonContext() {

        String html = """
                    <table>
                      <tr>
                        <td>John</td>
                        <td><button>Edit</button></td>
                      </tr>
                      <tr>
                        <td>Mary</td>
                        <td><button>Edit</button></td>
                      </tr>
                    </table>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.select("tr").get(1).selectFirst("button");

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(
                c.getLocator().getValue().contains("(//button)[2]") ||
                        c.getLocator().getValue().contains("tr")
        );
    }

    @Test
    void shouldResolveModalVsPageButton() {

        String html = """
                    <button>Save</button>
                    <div class="modal">
                        <button>Save</button>
                    </div>
                """;

        Document doc = Jsoup.parse(html);
        Element el = doc.select(".modal button").first();

        LocatorCandidate c = UniqueLocatorResolver.resolve(doc, el);

        assertTrue(c.getLocator().getValue().contains("modal"));
    }
}