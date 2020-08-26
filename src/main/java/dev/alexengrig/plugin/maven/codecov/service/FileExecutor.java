package dev.alexengrig.plugin.maven.codecov.service;

import dev.alexengrig.plugin.maven.codecov.exception.FileExecuteException;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FileExecutor {
    private final Consumer<? super String> outputConsumer;

    public FileExecutor(Consumer<? super String> outputConsumer) {
        this.outputConsumer = outputConsumer;
    }

    public int execute(File file, String token) throws FileExecuteException {
        String command = String.format("sh %s -t %s", file, token);
        try {
            Process process = Runtime.getRuntime().exec(command);
            StreamExchanger exchanger = new StreamExchanger(process.getInputStream(), outputConsumer);
            Executors.newSingleThreadExecutor().submit(exchanger);
            return process.waitFor();
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new FileExecuteException(file, e);
        }
    }

    private static class StreamExchanger implements Runnable {
        private final InputStream inputStream;
        private final Consumer<? super String> lineConsumer;

        public StreamExchanger(InputStream inputStream, Consumer<? super String> lineConsumer) {
            this.inputStream = inputStream;
            this.lineConsumer = lineConsumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(lineConsumer);
        }
    }
}
