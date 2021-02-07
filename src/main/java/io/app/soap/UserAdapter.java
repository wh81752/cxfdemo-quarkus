package io.app.soap;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 *
 * @author geronimo1
 */
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class UserAdapter extends XmlAdapter<UserImpl, User> {
    public UserImpl marshal(User v) throws Exception {
        if (v instanceof UserImpl) {
            return (UserImpl)v;
        }
        return new UserImpl(v.getName());
    }

    public User unmarshal(UserImpl v) throws Exception {
        return v;
    }
}
