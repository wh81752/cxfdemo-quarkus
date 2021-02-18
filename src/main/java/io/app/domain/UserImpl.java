package io.app.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 */
import org.wildfly.common.annotation.Nullable;

@XmlType(name = "User")
public class UserImpl implements User {
  String name;

  public UserImpl() {}

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
