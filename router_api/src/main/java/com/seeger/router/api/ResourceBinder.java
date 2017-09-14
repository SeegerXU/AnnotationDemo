package com.seeger.router.api;

/**
 * @author Seeger
 */
public interface ResourceBinder<T> {
    void bindResource(T host, Object object, ResourceFinder finder);

    void unBindResource(T host);
}
