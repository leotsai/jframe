package org.jframe.services.api;

import org.jframe.data.entities.User;

/**
 * Created by leo on 2017-05-31.
 */
public interface UserService {
    void register(User user) throws Exception;
}
