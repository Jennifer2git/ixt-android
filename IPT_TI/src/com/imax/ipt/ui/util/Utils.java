package com.imax.ipt.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Utils {
    private static String join(String delimiter, Iterable<? extends Object> objs) {

        Iterator<? extends Object> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(iter.next());
        while (iter.hasNext()) {
            buffer.append(delimiter).append(iter.next());
        }
        return buffer.toString();
    }

    // for convenience
    public static String join(String delimiter, String[] options) {
        if (options == null) {
            return "";
        }
        ArrayList<Object> list = new ArrayList<Object>();
        Collections.addAll(list, options);
        return join(delimiter, list);
    }
}
