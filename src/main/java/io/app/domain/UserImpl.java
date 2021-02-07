package io.app.domain;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 *
 * @author geronimo1
 */
import org.wildfly.common.annotation.Nullable;

import javax.xml.bind.annotation.XmlType;


@XmlType(name = "User")
public class UserImpl implements User {
    String name;

    public UserImpl() {
    }
    public UserImpl(@Nullable String s) {
        setName(s);
    }

    public @Nullable String getName() {
        return name;
    }

    public void setName(@Nullable String s) {
        if (s != null) {
            this.name = s.trim();
        }
    }
}