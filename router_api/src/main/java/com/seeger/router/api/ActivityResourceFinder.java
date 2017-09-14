package com.seeger.router.api;

import android.app.Activity;

/**
 * @author Seeger
 */
public class ActivityResourceFinder implements ResourceFinder {
    @Override
    public String findResource(Object object, int id) {
        return ((Activity) object).getResources().getString(id);
    }
}
