package org.jframe.data.caching;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.extensions.JDate;
import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.core.logging.LogHelper;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.DbCache;
import org.jframe.data.enums.DbCacheKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leo on 2017/11/28.
 */
public class DbCacheContext implements AppInitializer {

    public final static int INTERVAL_SECONDS = 10;

    private final static DbCacheContext instance = new DbCacheContext();

    public static DbCacheContext getInstance() {
        return instance;
    }

    private boolean isStarted = false;
    private final Map<DbCacheKey, Long> map = new ConcurrentHashMap<>();
    private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

    private DbCacheContext() {

    }

    public DbCacheContext initializeAll() {
        long version = JDate.now().getVersionSeconds();
        JList<DbCacheKey> cacheKeys = JList.from(DbCacheKey.values());
        try (JframeDbContext db = new JframeDbContext()) {
            JList<DbCache> dbCaches = db.getDbCacheSet().getAll();
            JList<DbCacheKey> addedList = cacheKeys.where(x -> !dbCaches.any(d -> d.getCacheKey().equals(x)));
            JList<DbCache> deletedList = dbCaches.where(d -> !cacheKeys.any(r -> r.equals(d.getCacheKey())));
            for (DbCacheKey key : addedList) {
                db.save(new DbCache(key, version));
            }
            deletedList.forEach(item -> db.delete(item));

            db.commitTransaction();
            System.out.println("    初始化DbCacheContext. 增加: " + addedList.size() + ", 删除: " + deletedList.size());
        }
        this.refresh();
        this.getContexts().forEach(x -> x.initialize(version));
        return this;
    }

    public void start() {
        if (this.isStarted) {
            return;
        }
        this.pool.scheduleAtFixedRate(() -> onTimerElapsed(), INTERVAL_SECONDS, INTERVAL_SECONDS, TimeUnit.SECONDS);
        this.isStarted = true;
    }

    public void stop() {
        this.pool.shutdown();
        this.isStarted = false;
    }

    private void refresh() {
        JMap<DbCacheKey, Long> jMap;
        try (JframeDbContext db = new JframeDbContext()) {
            jMap = db.getDbCacheSet().getAll().toMap(x -> x.getCacheKey(), x -> x.getVersion());
        }
        this.map.clear();
        this.map.putAll(jMap);
    }

    private void onTimerElapsed() {
        try {
            this.doWork();
        } catch (Exception ex) {
            LogHelper.log("DbCacheContext", ex);
        }
    }

    private void doWork() {
        this.refresh();
        this.getContexts().forEach(context -> {
            long dbVersion = this.map.get(context.getCacheKey());
            if (context.getVersion() < dbVersion) {
                context.refresh(dbVersion);
            }
        });
    }

    private JList<VersionedCacheContext> getContexts() {
        JList<VersionedCacheContext> list = new JList<>();
        list.add(RoleContext.getInstance());
        list.add(SettingContext.getInstance());
        return list;
    }

    public void refreshDbVersion(DbCacheKey key) {
        try (JframeDbContext db = new JframeDbContext()) {
            db.getDbCacheSet().refresh(key);
            db.commitTransaction();
        }
    }

    @Override
    public String init() {
        this.initializeAll().start();
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {
        this.stop();
    }
}
