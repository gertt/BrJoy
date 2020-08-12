package com.browsejoy.games.app.utils.hmac;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import static com.unity3d.ads.misc.Utilities.toHexString;

/**
 * Created by Gerti on 10/18/2018.
 */

public class Hmac {

    protected String HmacKey = "C8IzutuTNtlaETdyjx7fqPGhO4W9hcJk1d3MPkRA";

    private final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final String getEpochTime(){

        long millis = System.currentTimeMillis();

        long seconds = millis / 1000;

        return String.valueOf(seconds);
    }
    public String nonce;

    public Hmac() {
        this.nonce = getEpochTime();
    }

    public String calculateRFC2104HMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    public String generateHmac()
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        String entirePayloadToHash = "/api/v1/user/activity" + this.nonce;

        return  this.calculateRFC2104HMAC(entirePayloadToHash, this.HmacKey);

    }
}

