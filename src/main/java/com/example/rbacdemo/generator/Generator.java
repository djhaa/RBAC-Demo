package com.example.rbacdemo.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成pojo实体类、mapper接口、xml映射文件
 * @author djhaa
 */
public class Generator {
    public static void main(String[] args) throws XMLParserException, IOException, SQLException, InterruptedException, InvalidConfigurationException {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        InputStream in = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(in);
        assert in != null;
        in.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator mbg = new MyBatisGenerator(config, callback, warnings);
        mbg.generate(null);

        for(String warning:warnings){
            System.out.println(warning);
        }
    }
}
