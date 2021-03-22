package com.supcon.mes.module_map.util;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Time:    2020-05-30  14: 31
 * Author： nina
 * Des:
 */
public class DisposeUtil {
    private static DisposeUtil instance;

    private Map<String, Disposable> mapDispose = new HashMap<>();

    private DisposeUtil() {

    }

    public static DisposeUtil getInstance() {
        if (instance == null) {
            instance = new DisposeUtil();
        }
        return instance;
    }

    public void addKey(String key, Disposable disposable) {
        mapDispose.put(key, disposable);
    }


    public void cacelAll() {
        if (mapDispose != null && mapDispose.size() > 0) {
            for (Map.Entry<String, Disposable> stringDisposableEntry : mapDispose.entrySet()) {
                Disposable disposable = stringDisposableEntry.getValue();
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        }
    }

    public void cacelByKey(String key) {
        Disposable disposable = mapDispose.get(key);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
