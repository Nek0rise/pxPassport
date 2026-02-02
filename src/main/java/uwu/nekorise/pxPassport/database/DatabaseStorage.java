package uwu.nekorise.pxPassport.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

public class DatabaseStorage {

    private static final String DB_NAME = "pxpassport";
    private final HikariDataSource dataSource;

    private DatabaseStorage(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        createTable();
    }

    public static DatabaseStorage mysql(String host, int port, String user, String password) {

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf8");
        cfg.setUsername(user);
        cfg.setPassword(password);

        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(10000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        return new DatabaseStorage(new HikariDataSource(cfg));
    }

    public static DatabaseStorage h2(String folderPath) {

        HikariConfig cfg = new HikariConfig();

        cfg.setJdbcUrl("jdbc:h2:file:" + folderPath + "/database;MODE=MySQL;DATABASE_TO_UPPER=false");
        cfg.setDriverClassName("org.h2.Driver");
        return new DatabaseStorage(new HikariDataSource(cfg));
    }

    private void createTable() {
        execute("""
                    CREATE TABLE IF NOT EXISTS passports (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nickname VARCHAR(32) NOT NULL UNIQUE,
                        author VARCHAR(32) NOT NULL,
                        date DATE NOT NULL,
                        birthday VARCHAR(32),
                        city VARCHAR(64),
                        sex VARCHAR(64),
                        avatar LONGTEXT NOT NULL,
                        value1 TEXT,
                        value2 TEXT,
                        value3 TEXT,
                        value4 TEXT,
                        value5 TEXT,
                        value6 TEXT,
                        value7 TEXT,
                        value8 TEXT,
                        value9 TEXT,
                        value10 TEXT
                    )
                """);
    }

    private static final String BASE_UPSERT = """
                INSERT INTO passports (nickname, author, date, avatar)
                VALUES (?, '', CURDATE(), '')
                ON DUPLICATE KEY UPDATE nickname = nickname
            """;

    private void ensureExists(String nickname) {
        upsert(BASE_UPSERT, ps -> ps.setString(1, nickname));
    }

    public void updateAuthor(String nickname, String author) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }
        updateSingle("author", nickname, author);
    }

    public void updateBirthday(String nickname, String birthday) {
        updateSingle("birthday", nickname, birthday);
    }

    public void updateCity(String nickname, String city) {
        updateSingle("city", nickname, city);
    }

    public void updateSex(String nickname, String sex) {
        updateSingle("sex", nickname, sex);
    }

    public void updateAvatar(String nickname, String avatar) {
        updateSingle("avatar", nickname, avatar);
    }

    private void updateSingle(String field, String nickname, String value) {
        ensureExists(nickname);
        upsert("UPDATE passports SET " + field + " = ?, date = CURDATE() WHERE nickname = ?", ps -> {
            ps.setString(1, value);
            ps.setString(2, nickname);
        });
    }

    public void updateValues(String nickname, Map<Integer, String> values) {
        ensureExists(nickname);

        StringBuilder sql = new StringBuilder("UPDATE passports SET ");
        boolean first = true;

        for (int i = 1; i <= 10; i++) {
            if (!first) sql.append(", ");
            sql.append("value").append(i).append(" = ?");
            first = false;
        }

        sql.append(", date = CURDATE() WHERE nickname = ?");

        upsert(sql.toString(), ps -> {
            int index = 1;
            for (int i = 1; i <= 10; i++) {
                ps.setString(index++, values.get(i));
            }
            ps.setString(index, nickname);
        });
    }

    public boolean deletePassport(String nickname) {
        return executeUpdate("DELETE FROM passports WHERE nickname = ?", ps -> ps.setString(1, nickname)) > 0;
    }

    public Integer getId(String nickname) {
        return querySingle("SELECT id FROM passports WHERE nickname = ?", ps -> ps.setString(1, nickname), rs -> rs.getInt("id"));
    }

    public LocalDate getDate(String nickname) {
        return querySingle("SELECT date FROM passports WHERE nickname = ?", ps -> ps.setString(1, nickname), rs -> rs.getDate("date").toLocalDate());
    }

    public String getAuthor(String nickname) {
        return getString("author", nickname);
    }

    public String getBirthday(String nickname) {
        return getString("birthday", nickname);
    }

    public String getCity(String nickname) {
        return getString("city", nickname);
    }

    public String getSex(String nickname) {
        return getString("sex", nickname);
    }

    public String getAvatar(String nickname) {
        return getString("avatar", nickname);
    }

    public String getValue(String nickname, int index) {
        if (index < 1 || index > 10) {
            throw new IllegalArgumentException("value index must be 1..10");
        }

        return querySingle("SELECT value" + index + " FROM passports WHERE nickname = ?", ps -> ps.setString(1, nickname), rs -> rs.getString(1));
    }

    private String getString(String field, String nickname) {
        return querySingle("SELECT " + field + " FROM passports WHERE nickname = ?", ps -> ps.setString(1, nickname), rs -> rs.getString(field));
    }

    public boolean exists(String nickname) {
        return querySingle("SELECT 1 FROM passports WHERE nickname = ? LIMIT 1", ps -> ps.setString(1, nickname), rs -> true) != null;
    }

    private void execute(String sql) {
        try (Connection con = dataSource.getConnection(); Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(String sql, SQLConsumer<PreparedStatement> consumer) {
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            consumer.accept(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void upsert(String sql, SQLConsumer<PreparedStatement> consumer) {
        executeUpdate(sql, consumer);
    }

    private <T> T querySingle(String sql, SQLConsumer<PreparedStatement> consumer, SQLFunction<ResultSet, T> mapper) {
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapper.apply(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        dataSource.close();
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    @FunctionalInterface
    private interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
