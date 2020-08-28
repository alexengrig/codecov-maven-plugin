/*
 * Copyright 2020 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.codecov.maven.service;

import dev.alexengrig.codecov.maven.exception.CommandExecuteException;

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
