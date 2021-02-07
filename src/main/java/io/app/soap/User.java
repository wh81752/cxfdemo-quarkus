package io.app.soap;

/**
 * This interface defines capabilities of an user.
 *
 */
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(UserAdapter.class)
public interface User {
    String getName();
}
