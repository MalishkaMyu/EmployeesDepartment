package com.samsolutions.employeesdep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class TestDataHelper {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void executeSqlScript(Resource sqlScriptFile) throws IOException {
        String sqlScript = Files.readString(Paths.get(sqlScriptFile.getURI()),
                StandardCharsets.UTF_8);
        jdbcTemplate.execute(sqlScript);
    }
}
