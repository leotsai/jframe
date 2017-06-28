package org.jframe.services.api.impl;

import org.jframe.AppContext;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.User;
import org.jframe.infrastructure.core.KnownException;
import org.jframe.services.api.UserService;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

/**
 * Created by leo on 2017-05-31.
 */
@Service("api-user-service")
public class UserServiceImpl extends ServiceBase implements UserService {
    @Override
    public void register(User user) throws Exception {
        user.validateOnRegister();
        try(JframeDbContext db = new JframeDbContext()){
            User newUser = user.toRegisteringDefault();
            if(db.getUserSet().existsUsername(newUser.getUsername())){
                throw new KnownException("username already registered.");
            }
            newUser.setRegisterIp(AppContext.getCurrentRequest().getRemoteUser());
            db.save(newUser);
            db.commitTransaction();
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
