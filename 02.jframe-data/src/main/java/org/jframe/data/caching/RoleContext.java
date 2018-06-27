package org.jframe.data.caching;

import org.jframe.core.extensions.JList;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Role;
import org.jframe.data.enums.DbCacheKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Leo on 2017/11/17.
 */
public class RoleContext implements VersionedCacheContext {

    private final static RoleContext instance = new RoleContext();

    public static RoleContext getInstance() {
        return instance;
    }


    private long version;
    private final Map<String, JList<String>> map = new ConcurrentHashMap<>();
    private RoleContext() {

    }

    @Override
    public void initialize(long version) {
        Role.initSystemRoles();
        this.refresh(version);
    }

    @Override
    public DbCacheKey getCacheKey() {
        return DbCacheKey.ROLE;
    }

    public JList<String> getPermissions(String roleName) {
        return this.map.get(roleName);
    }

    @Override
    public void refresh(long version) {
        this.version = version;
        try (JframeDbContext db = new JframeDbContext()) {
            this.map.clear();
            JList<Role> roles = db.set(Role.class).getAll();
            for(Role role : roles){
                this.map.put(role.getName(), role.getCodes());
            }
        }
    }

    @Override
    public long getVersion() {
        return version;
    }
}
