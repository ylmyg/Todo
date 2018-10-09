package com.xujiaji.todo.listener;

/**
 * author: xujiaji
 * created on: 2018/10/9 22:12
 * description:
 */
public interface GenericEventListener<T> {
    void event(T t);
}
