//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.javabackend.backend.util;

import java.text.MessageFormat;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

public class LinkHeaderUtil {
    private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

    public LinkHeaderUtil() {
    }

    public String prepareLinkHeaders(UriComponentsBuilder uriBuilder, Page<?> page) {
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        StringBuilder link = new StringBuilder();
        if (pageNumber < page.getTotalPages() - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
        }

        if (pageNumber > 0) {
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
        }

        link.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last")).append(",").append(prepareLink(uriBuilder, 0, pageSize, "first"));
        return link.toString();
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format("<{0}>; rel=\"{1}\"", preparePageUri(uriBuilder, pageNumber, pageSize), relType);
    }

    private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder.replaceQueryParam("page", new Object[]{Integer.toString(pageNumber)}).replaceQueryParam("size", new Object[]{Integer.toString(pageSize)}).toUriString().replace(",", "%2C").replace(";", "%3B");
    }
}
