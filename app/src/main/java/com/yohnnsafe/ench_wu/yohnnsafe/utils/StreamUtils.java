package com.yohnnsafe.ench_wu.yohnnsafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ench_Wu on 2015/8/28.
 */
public class StreamUtils {

    public static String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) > 0) {
            out.write(bytes, 0, len);
        }

        String result = out.toString();
        in.close();
        out.close();
        return result;
    }
}
