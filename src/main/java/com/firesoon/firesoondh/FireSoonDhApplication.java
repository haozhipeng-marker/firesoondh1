package com.firesoon.firesoondh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Yuan
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan({"com.firesoon.firesoondh.mapper"})
public class FireSoonDhApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireSoonDhApplication.class, args);
    }

}
