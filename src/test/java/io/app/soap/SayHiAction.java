package io.app.soap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 *
 * @author geronimo1
 */
@XmlRootElement(name = "sayHi",namespace = "http://soap.app.io/")
public class SayHiAction {
    @XmlElement(name = "arg0", required = true, nillable = false)
    public String payload;
}
