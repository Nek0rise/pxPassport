package uwu.nekorise.pxPassport.database;

import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.util.Log;

public final class DatabaseService {

    private DatabaseStorage storage;

    public void init() {
        if (storage != null) {
            throw new IllegalStateException("Database service already initialized");
        }

        DatabaseType type = MainConfigStorage.getDBMS();
        storage = switch (type) {
            case MYSQL -> DatabaseStorage.mysql(MainConfigStorage.getDbHost(), MainConfigStorage.getDbPort(), MainConfigStorage.getDbUser(), MainConfigStorage.getDbPass());
            case H2 -> DatabaseStorage.h2(PxPassport.getInstance().getDataFolder().getAbsolutePath());
        };

        Log.info(PxPassport.getInstance(), "Database initialized using " + type.name());
    }

    public DatabaseStorage getStorage() {
        if (storage == null) {
            throw new IllegalStateException("Database service not initialized");
        }
        return storage;
    }

    public void shutdown() {
        if (storage == null) return;

        storage.shutdown();
        storage = null;

        Log.info(PxPassport.getInstance(), "Database connection closed");
    }
}
