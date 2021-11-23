package com.example.oauthlearning.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {
    private Integer id;
    private String username;
    private boolean isAdmin;
}
