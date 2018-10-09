package com.xujiaji.todo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author: xujiaji
 * created on: 2018/8/11 18:06
 * description:
 */
public class NetUtil {
    public static String encodeCookie(List<String> cookies) {
        final StringBuilder sb = new StringBuilder();
        final Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            set.addAll(Arrays.asList(cookie.split(";")));
        }
        for (String s : set) {
            sb.append(s).append(";");
        }
        if (sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString().trim();
    }


    /**
     * 系统浏览器打开该链接
     * @param url 链接
     */
    public static void systemBrowserOpen(Context context, String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
