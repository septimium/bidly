package com.example.bidly.services;


import com.example.bidly.entity.AppUser;
import com.example.bidly.entity.Role;
import com.example.bidly.repositories.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public AppUser authenticateUser(String email, String rawPassword) {
        return appUserRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(null);
    }

    @Override
    public AppUser registerUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        return appUserRepository.save(user);
    }


    @Override
    public boolean emailExists(String email) {
        return appUserRepository.findByEmail(email).isPresent();
    }
}
