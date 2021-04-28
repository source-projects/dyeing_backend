package com.main.glory.schedulers;

import com.cloudinary.Cloudinary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingletonManager {
    private Cloudinary cloudinary;

    public void setCloudinary(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public void init() {
        Singleton.registerCloudinary(cloudinary);
    }

    public void destroy() {
        Singleton.deregisterCloudinary();
    }
}
