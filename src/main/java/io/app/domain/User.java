package io.app.domain;

/**
 * This interface defines capabilities of an user.
 *
 */
import io.app.soap.UserAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(UserAdapter.class)
public interface User {
    String getName();
}
