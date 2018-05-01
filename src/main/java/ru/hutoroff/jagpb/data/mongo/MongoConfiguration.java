package ru.hutoroff.jagpb.data.mongo;

public class MongoConfiguration {
    private String host = "localhost";
    private Integer port = 27017;
    private String db = "test";
    private MongoAuthConfiguration auth;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public MongoAuthConfiguration getAuth() {
        return auth;
    }

    public void setAuth(MongoAuthConfiguration auth) {
        this.auth = auth;
    }
}
