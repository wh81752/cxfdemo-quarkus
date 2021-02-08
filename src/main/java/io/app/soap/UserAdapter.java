package io.app.soap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 */
import io.app.domain.User;
import io.app.domain.UserImpl;

public class UserAdapter extends XmlAdapter<UserImpl, User> {
    public UserImpl marshal(User v) throws Exception {
        if (v instanceof UserImpl) {
            return (UserImpl) v;
        }
        return new UserImpl(v.getName());
    }

    public User unmarshal(UserImpl v) throws Exception {
        return v;
    }
}
