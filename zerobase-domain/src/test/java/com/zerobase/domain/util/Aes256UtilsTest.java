package com.zerobase.domain.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Aes256UtilsTest {

    @Test
    void encrypt() {
        String encrypt = Aes256Utils.encrypt("Hello world");
        assertEquals(Aes256Utils.decrypt(encrypt), "Hello world");
    }

}