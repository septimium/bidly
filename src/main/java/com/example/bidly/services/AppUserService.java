package com.example.bidly.services;


import com.example.bidly.entity.AppUser;

public interface AppUserService {

    AppUser authenticateUser(String email, String rawPassword);
    AppUser registerUser(AppUser user);
    boolean emailExists(String email);

}
