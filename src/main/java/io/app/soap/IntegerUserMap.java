package io.app.soap;

import io.app.domain.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 */
@XmlType(name = "IntegerUserMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerUserMap {
    @XmlElement(nillable = false, name = "entry")
    List<IntegerUserEntry> entries = new ArrayList<>();

    public List<IntegerUserEntry> getEntries() {
        return entries;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "IdentifiedUser")
    static class IntegerUserEntry {
        //Map keys cannot be null
        @XmlElement(required = true, nillable = false)
        Integer id;

        User user;

        public void setId(Integer k) {
            id = k;
        }

        public Integer getId() {
            return id;
        }

        public void setUser(User u) {
            user = u;
        }

        public User getUser() {
            return user;
        }
    }
}
