package io.app.soap;

import org.hamcrest.Matcher;
import org.w3c.dom.Document;
import org.xmlunit.matchers.HasXPathMatcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

/**
 * XmlNs Documentation
 *
 * <p>
 * What is the point of this class?
 *
 * @author geronimo1
 */
public final class Xml {

    /**
     * SOAP Namespaces
     *
     * @return unmodifiable map
     */
    public static Map<String, String> soapNamespaceContext() {
        Map<String, String> m = new HashMap<>();
        m.put("http", "http://schemas.xmlsoap.org/ws/http");
        m.put("soap", "http://schemas.xmlsoap.org/wsdl/ws/");
        m.put("wsdl", "http://schemas.xmlsoap.org/wsdl/");
        m.put("xsd", "http://www.w3.org/2001/XMLSchema");
        m.put("senv", "http://schemas.xmlsoap.org/ws/envelope/");
        return Collections.unmodifiableMap(m);
    }

    public static Map<String, String> appNamespaceContext() {
        Map<String, String> m = new HashMap<>();
        m.put("app", "http://ws.app.io/");
        return Collections.unmodifiableMap(m);
    }

    /**
     * Provides a Hamcrest WSDL matcher.
     *
     * @return
     */
    public static HasXPathMatcher matcherWsdl() {
        return hasXPath("/wsdl:definitions").withNamespaceContext(soapNamespaceContext());
    }

    public enum Matchers {
        WSDL(matcherWsdl());

        @SuppressWarnings("rawtypes")
        public final Matcher matcher;

        Matchers(Matcher m) {
            Objects.requireNonNull(m);
            this.matcher = m;
        }
    }

    public enum NsXPath {
        // APP
        APP("http://ws.app.io/"),
        // SOAP
        SOAP("http://schemas.xmlsoap.org/wsdl/soap/"),
        WSDL("http://schemas.xmlsoap.org/wsdl/"),
        XSD("http://www.w3.org/2001/XMLSchema"),
        ENV("http://schemas.xmlsoap.org/soap/envelope/"),
        HTTP("http://schemas.xmlsoap.org/soap/http");

        public final String namespace;

        NsXPath(String namespace) {
            this.namespace = namespace;
        }

        public static Map<String, String> ns() {
            Map<String, String> m = new HashMap<>();
            Arrays.stream(NsXPath.values()).forEach(nsXPath -> m.put(nsXPath.name(), nsXPath.namespace));
            return m;
        }
    }

    public enum Namespaces {
        APP(appNamespaceContext()),
        SOAP(soapNamespaceContext());

        public final Map<String, String> nsmap;

        Namespaces(Map<String, String> ns) {
            Objects.requireNonNull(ns);
            this.nsmap = ns;
        }

        public static Map<String, String> combine(Namespaces... namespaces) {
            Map<String, String> m = new HashMap<>();
            return Arrays.stream(namespaces).map(ns -> ns.nsmap).reduce(m, (accu, item) -> {
                accu.putAll(item);
                return accu;
            });
        }
    }

    /**
     * SOAPREQUESTS contains a collection of ready to go SOAP requests for webservice
     * <code>HelloWorld</code>.
     *
     * <p>
     * The request message are ready to be send over http to a SOAP endpoint service implementing service HelloWorld.
     *
     * <p>
     * Not all service messages are implemented by reasons outlined further below. By now
     * <ul>
     * <li><code>hello()</code>; and</li>
     * <li><code>sayHi()</code></li>
     * </ul>
     * are available.
     *
     * <p>
     * The implementation shows the principal way of constructing such a SOAP request. It can then
     * easily used for testing for example with RestAssured like so:
     *
     * <pre>
     *  {@code
     *  import static io.restassured.RestAssured.given;
     *  ..
     *  String body = Xml.SOAPREQUESTS.sayHi("foo");
     *  given().[..].body().when(body).post("/ws/hw/sayHi").then().[..];
     *  }
     * </pre>
     *
     * <p>
     * Although this works it is rather cumbersome and errorprone to use this approach for testing or
     * other means:
     * <ol>
     * <li>Soap action wrapper classes like <code>HelloAction</code>, <code>SayHiAction</code> et
     * cetera must be implemented manually;
     * <li>Changing minor contract details -- say sayHi's webmethod parameter for example -- must be
     * reflected within class <code>SayHiAction</code>
     * </ol>
     * Hence a automatism is necessary to hide this details from the developer. This is where SOAP
     * service frameworks like CXF kick in. They take care about this.
     *
     * <p>
     * Apache CXF makes it extremly easy to call a webservice method on the client side by hiding all
     * the details. On the debit side, simply creating a SOAP request string like demonstrated in this
     * class appears to be difficult (no references in Internet found so far).
     */
    public static class SOAPREQUESTS {
        static public String hello() {
            HelloAction hello = new HelloAction();
            return soapRequest(hello, HelloAction.class);
        }

        static public String sayHi(String text) {
            SayHiAction hello = new SayHiAction();
            hello.payload = text;
            return soapRequest(hello, SayHiAction.class);
        }

        static public <T> String soapRequest(
                T obj,
                Class<T> tClass
        ) {
            try {
                return toString(createNew(marshal(obj, tClass)));
            } catch (JAXBException | ParserConfigurationException | SOAPException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static Document marshal(
                Object obj,
                Class<?> clazz
        )
                throws ParserConfigurationException, JAXBException {
            Marshaller m;
            Document d;
            d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            m = JAXBContext.newInstance(clazz).createMarshaller();
            m.marshal(obj, d);
            return d;
        }

        /**
         * Create empty SOAP message with namespace prefix <code>soap</code>
         */
        private static SOAPMessage createNew() throws SOAPException {
            SOAPMessage m;
            final String soapNs = "http://www.w3.org/2001/12/soap-envelope";
            final String pfx = "soap";
            m = MessageFactory.newInstance().createMessage();
            // Replace default namespace prefix SOAP-ENV with soap. Quite some
            // operations are necessary.
            m.getSOAPPart().getEnvelope().removeNamespaceDeclaration("SOAP-ENV");
            m.getSOAPPart().getEnvelope().addNamespaceDeclaration(pfx, soapNs);
            m.getSOAPPart().getEnvelope().setPrefix(pfx);
            m.getSOAPHeader().setPrefix(pfx);
            m.getSOAPBody().setPrefix(pfx);
            return m;
        }

        /**
         * Create new SOAP message where message's body is given document.
         */
        private static SOAPMessage createNew(Document doc) throws SOAPException {
            SOAPMessage m = createNew();
            m.getSOAPBody().addDocument(doc);
            return m;
        }

        private static String toString(SOAPMessage m) throws IOException, SOAPException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            m.writeTo(outputStream);
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        }

    }


}
