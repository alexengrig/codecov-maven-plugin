package dev.alexengrig.codecov.maven.plugin.service;

import dev.alexengrig.codecov.maven.plugin.exception.CommandExecuteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CommandExecutor {
    private final Consumer<? super String> outputConsumer;

    public CommandExecutor(Consumer<? super String> outputConsumer) {
        this.outputConsumer = outputConsumer;
    }

    public int execute(String command) throws CommandExecuteException {
        try {
            Process process = Runtime.getRuntime().exec(command);
            StreamExchanger exchanger = new StreamExchanger(process.getInputStream(), outputConsumer);
            Executors.newSingleThreadExecutor().submit(exchanger);
            return process.waitFor();
        } catch (InterruptedException | IOException ignore) {
            Thread.currentThread().interrupt();
            throw new CommandExecuteException();
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
