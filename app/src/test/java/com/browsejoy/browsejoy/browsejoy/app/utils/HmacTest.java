package com.browsejoy.browsejoy.browsejoy.app.utils;

import com.browsejoy.browsejoy.browsejoy.app.utils.hmac.Hmac;

import org.junit.Test;

import static org.junit.Assert.*;

public class HmacTest {
    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
    public void hmac_has_header_info() throws Exception {
        Hmac hmac = new Hmac();
        String s = hmac.generateHmac();
        assertNotEquals(s, "");
        // breakpoint here to manually verify
    }
}
