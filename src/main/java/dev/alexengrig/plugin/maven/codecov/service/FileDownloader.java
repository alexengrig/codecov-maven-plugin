package dev.alexengrig.plugin.maven.codecov.service;

import dev.alexengrig.plugin.maven.codecov.exception.FileDownloadException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
    /**
     * Download file from url to file.
     *
     * @param url  from
     * @param file to
     * @throws FileDownloadException I/O errors
     */
    public void download(URL url, File file) throws FileDownloadException {
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new FileDownloadException(url, file, e);
        }
    }
}
