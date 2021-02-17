package io.app.xmltest;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.xmlunit.matchers.CompareMatcher;

/**
 * XmlUnitTests Documentation
 *
 * <p>
 * Test (some) functionality of XmlUnit (https://www.xmlunit.org/)
 *
 */
public class XmlUnitTests {

  @Test
  public void test_not_identical() {
    final String cntl = "<a><b attr=\"abc\"></b></a>";
    final String test = cntl.replace("abc", "xyz");

    assertThat(test, not(CompareMatcher.isIdenticalTo(cntl)));
  }

  @Test
  public void test_identical_ignoring_attr() {
    final String cntl = "<a><b attr=\"abc\"></b></a>";
    final String test = cntl.replace("abc", "xyz");

    assertThat(test, isSimilarTo(cntl)
        .withAttributeFilter((Attr attr) -> !attr.getName().equalsIgnoreCase("attr")));
  }

  /**
   * Test condition: XML document is empty, regardless of root name and any attributes on root.
   */
  @Test
  public void test_empty() {
    final String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><whatever xmlns:h=\"http://www.w3.org/TR/html4/\" a=\"1\"></whatever>";

    assertThat(xml, hasXPath("/node()"));
    assertThat(xml, hasXPath("/*"));
    assertThat(xml, not(hasXPath("/node()/node()")));
    assertThat(xml, not(hasXPath("/*/node()")));
    assertThat(xml, not(hasXPath("/*/*")));
  }

  /**
   * Test condition: non empty XML document
   */
  @Test
  public void test_notempty() {
    final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><whatever><foo/></whatever>";
    assertThat(xml, hasXPath("/whatever/node()"));
    assertThat(xml, hasXPath("/node()/node()"));
  }
}
