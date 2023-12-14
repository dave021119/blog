package com.jsp.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StrMD5 {
    public StrMD5 () {
    }

    public static String MD5 (String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance ("MD5");
            md.update (sourceStr.getBytes ());
            byte[] b = md.digest ();
            StringBuffer buf = new StringBuffer ("");
            for (int offset = 0; offset < b.length; ++ offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append ("0");
                }

                buf.append (Integer.toHexString (i));
            }
            result = buf.toString ();
        } catch (NoSuchAlgorithmException var7) {
            System.out.println (var7);
        }
        return result;
    }
}
