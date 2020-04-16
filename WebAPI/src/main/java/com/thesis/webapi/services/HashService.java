package com.thesis.webapi.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface HashService {

    String hashString(String string) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
