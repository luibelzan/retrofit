package com.celnet.retrofit.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import org.springframework.http.HttpHeaders;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders="*", methods={RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/backup")
public class BackupController {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadBackup() {
        try {
            String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1).split("\\?")[0];

            String host = dbUrl.split("//")[1].split(":")[0];

            String fileName = "backup_" + dbName + "_" + System.currentTimeMillis() + ".sql";
            File backupFile = new File(System.getProperty("java.io.tmpdir"), fileName);

            // Comando pg_dump
            String[] command = {
                "E:\\Archivos de programa\\PostgreSQL\\17\\bin\\pg_dump",
                "-h", host,
                "-U", dbUser,
                "-d", dbName,
                "-F", "p",
                "-f", backupFile.getAbsolutePath()
            };

            // Ejecutar comando
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("PG_DUMP >>> " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("EXIT CODE = " + exitCode);

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(backupFile));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .contentLength(backupFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
