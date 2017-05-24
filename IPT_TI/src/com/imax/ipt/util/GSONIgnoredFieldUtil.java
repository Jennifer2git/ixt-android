package com.imax.ipt.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GSONIgnoredFieldUtil implements ExclusionStrategy {

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getName().toLowerCase().contains("handler");
    }

}
