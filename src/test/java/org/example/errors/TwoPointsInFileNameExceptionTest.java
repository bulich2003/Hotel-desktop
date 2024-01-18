package org.example.errors;

import org.example.gui.frames.FileChooserFrame;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TwoPointsInFileNameExceptionTest {

    @BeforeAll
    static void setUp() { System.out.println("@BeforeAll executed"); }

    @Test
    void twoPointsInFileNameExceptionTest() {
        Throwable thrown = assertThrows(TwoPointsInFileNameException.class, () ->
                FileChooserFrame.checkPath("C:\\test\testFile.xml.xml", "recording"));
        assertEquals("Two points were used in the name of file", thrown.getMessage());

        System.out.println("TwoPointsInFileNameExceptionTest executed");
    }

    @AfterAll
    static void tearDown() { System.out.println("@AfterAll executed"); }
}