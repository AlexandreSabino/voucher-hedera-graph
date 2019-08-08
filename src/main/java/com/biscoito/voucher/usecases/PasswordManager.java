package com.biscoito.voucher.usecases;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class PasswordManager {

    public String encrypt(String pass) {
        return Hashing.sha256()
                .hashString(pass, StandardCharsets.UTF_8)
                .toString();
    }

    public boolean compare(String pass, String hash) {
        return encrypt(pass).equals(hash);
    }
}
