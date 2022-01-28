package com.chf.app.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chf.app.utils.FileUtil;

public interface ResponseUtil {

    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    static <X> ResponseEntity<List<X>> wrapPage(Page<X> page) {
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    static void outputMedia(String fileName, HttpServletResponse response) {
        MediaType mediaType = getMediaType(fileName);
        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        response.setContentType(mediaType.toString());
        File file = new File(fileName);
        output(file, response);
    }

    static void output(File file, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream(); InputStream is = new FileInputStream(file);) {
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MediaType getMediaType(String fileName) {
        String suffix = FileUtil.getSuffix(fileName);
        if (suffix != null) {
            switch (suffix) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "md":
                return MediaType.TEXT_MARKDOWN;
            case "mp4":
                return MediaType.valueOf("video/mp4");
            }
        }
        return null;
    }
}
