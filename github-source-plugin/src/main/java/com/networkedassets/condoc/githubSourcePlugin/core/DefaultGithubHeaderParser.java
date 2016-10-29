package com.networkedassets.condoc.githubSourcePlugin.core;


import com.networkedassets.condoc.githubSourcePlugin.core.boundary.GithubHeaderParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class DefaultGithubHeaderParser implements GithubHeaderParser {

    private int lastPage;
    private String header;

    @Override
    public int getLastPage(String header) {
        int lastPage = Objects.nonNull(this.header) ? this.lastPage : getLastPageFromHeader(header);
        this.lastPage = lastPage;
        this.header = header;
        return lastPage;

    }


    private int getLastPageFromHeader(String header) {

        try {
            return Arrays.asList(getUrlFromHeader(header).getQuery().split("&")).stream().filter(query -> query
                    .startsWith("page")).findFirst().map(el -> Arrays.copyOf(el.split("="), 2)).map(o -> Integer
                    .parseInt(o[1])).orElseThrow(RuntimeException::new);
        } catch (MalformedURLException e) {
            throw new InvalidHeaderException(e.toString());
        }

    }


    private URL getUrlFromHeader(String header) throws MalformedURLException {

        return new URL(Arrays.asList(header.split(",")).stream().filter(el -> el.contains("last")).findFirst().map(s
                -> Arrays.copyOf(s.split(";"), 1)).map(o -> o[0].replaceAll("[<>]", "")).orElseThrow
                (RuntimeException::new));
    }

}
