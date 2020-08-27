package dev.alexengrig.codecov.maven.plugin.demo;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MainTest {
    @Test
    public void should_return_true() {
        Main main = new Main();
        assertTrue(main.returnTrue());
    }
}