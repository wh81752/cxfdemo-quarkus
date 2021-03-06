package io.app.domain;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This interface defines capabilities of an user.
 */
import io.app.ws.UserAdapter;

@XmlJavaTypeAdapter(UserAdapter.class)
public interface User {
  String getName();
}
