package com.wip.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

@Configuration
public class UploadConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
     //
        String path = "src/main/resources/static/upload/imgs";
        String truePath=new File(path).getAbsolutePath()+File.separator;
        truePath=truePath.replaceAll( "\\\\",   "\\\\\\\\");
        registry.addResourceHandler("/admin/upload/imgs/**").addResourceLocations("file:"+truePath);

    }
}
