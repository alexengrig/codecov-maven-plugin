package dev.alexengrig.codecov.maven.plugin.demo;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class NoMainTest {
    @Test
    public void should_return_false() {
        NoMain noMain = new NoMain();
        assertFalse(noMain.returnFalse());
    }
}