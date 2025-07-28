package com.celnet.retrofit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DataSourceChecker {

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void testConnection() {
        System.out.println("🔍 Verificando conexión a la base de datos...");
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✔ AutoCommit: " + conn.getAutoCommit());
            System.out.println("✔ Transaction Isolation: " + conn.getTransactionIsolation());
            System.out.println("✔ Driver Name: " + conn.getMetaData().getDriverName());
            System.out.println("✔ URL: " + conn.getMetaData().getURL());
            System.out.println("✔ Username: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("❌ Error verificando la conexión a la base de datos:");
            e.printStackTrace();
        }
    }
}
