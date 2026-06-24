package cn.aurorian.ers.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class GeoResourceCrypto {
    private static final String MAGIC = "ENC:";

    private static final byte[] KEY = new byte[] {
        0x47, (byte) 0xE3, 0x2A, (byte) 0xB1, 0x7F, (byte) 0xC8, 0x55, (byte) 0x9D,
        0x11, (byte) 0xA4, 0x6E, (byte) 0xD2, 0x38, (byte) 0xF5, 0x0C, (byte) 0x82,
        0x73, (byte) 0x19, (byte) 0xE7, 0x44, (byte) 0xB6, 0x2D, (byte) 0x9A, 0x61
    };

    private GeoResourceCrypto() {}

    public static String decryptIfNeeded(String content) {
        if (content == null || !content.startsWith(MAGIC)) {
            return content;
        }

        try {
            String base64 = content.substring(MAGIC.length());
            byte[] encrypted = Base64.getDecoder().decode(base64);
            byte[] decrypted = xor(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt geo/animation resource", e);
        }
    }

    private static byte[] xor(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ KEY[i % KEY.length]);
        }
        return result;
    }
}
