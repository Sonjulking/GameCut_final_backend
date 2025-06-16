package com.gaeko.gamecut.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']?([^\"'>]+)[\"']?");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }
}
