package com.femcoders.electronify.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name","electronify" ,
                "api_key", "556452817547762",
                "api_secret", "QCoDx1xRmB55zW-0de1p0ax4tGA"
        ));
    }
}