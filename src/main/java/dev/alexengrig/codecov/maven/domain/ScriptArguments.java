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

package dev.alexengrig.codecov.maven.domain;

import java.util.StringJoiner;

/**
 * <a href="https://docs.codecov.io/docs/about-the-codecov-bash-uploader#arguments">Arguments</a>.
 */
public class ScriptArguments {
    /**
     * {@code -d} : Don't upload, but dump upload file to stdout.
     */
    private boolean notUpload;

    /**
     * {@code -Z} : Exit with 1 if not successful. Default will Exit with 0.
     */
    private boolean exitCode = true;

    /**
     * {@code -v} : Verbose mode.
     */
    private boolean verbose;

    /**
     * {@code -t TOKEN} : Set the private repository token.
     * <pre>{@code
     * -t uuid
     * -t @/path/to/token_file
     * }</pre>
     */
    private String token;

    /**
     * {@code -s DIR} : Directory to search for coverage reports.
     * Already searches project root and artifact folders.
     */
    private String reportDirectory;

    /**
     * {@code -f FILE} : Target file(s) to upload
     * -f "path/to/file" only upload this file
     * skips searching unless provided patterns below:
     * <p>
     * -f '!*.bar' ignore all files at pattern .bar;
     * <p>
     * -f '.foo' include all files at pattern *.foo Must use single quotes.
     * This is non-exclusive, use-s "*.foo"` to match specific paths.
     */
    private String target;

    /**
     * {@code -F FLAG} : Flag the upload to group coverage metrics
     * <p>
     * -F unittests This upload is only unittests
     * <p>
     * -F integration This upload is only integration tests
     * <p>
     * -F ui,chrome This upload is Chrome - UI tests
     */
    private String metrics;

    /**
     * {@code -R ROOT_DIR} : Used when not in git/hg project to identify project root directory.
     */
    private String rootDirectory;

    /**
     * {@code -q PATH} : Write upload file to path.
     */
    private String writePath;

    /**
     * {@code -n NAME} : Custom defined name of the upload. Visible in Codecov UI.
     */
    private String name;

    @Override
    public String toString() {
        return "ScriptArguments{" +
                "exitCode=" + exitCode +
                ", verbose=" + verbose +
                ", token='" + token + '\'' +
                ", reportDirectory='" + reportDirectory + '\'' +
                ", target='" + target + '\'' +
                ", metrics='" + metrics + '\'' +
                ", rootDirectory='" + rootDirectory + '\'' +
                ", writePath='" + writePath + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String inline() {
        StringJoiner joiner = new StringJoiner(" ");
        if (notUpload) joiner.add("-d");
        if (exitCode) joiner.add("-Z");
        if (verbose) joiner.add("-v");
        if (token != null) joiner.add("-t").add(token);
        if (reportDirectory != null) joiner.add("-s").add(reportDirectory);
        if (target != null) joiner.add("-f").add(target);
        if (metrics != null) joiner.add("-F").add(metrics);
        if (rootDirectory != null) joiner.add("-R").add(rootDirectory);
        if (writePath != null) joiner.add("-q").add(writePath);
        if (name != null) joiner.add("-n").add(name);
        return joiner.toString();
    }
}
