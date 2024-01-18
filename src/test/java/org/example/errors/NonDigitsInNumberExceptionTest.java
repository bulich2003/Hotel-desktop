package org.example.errors;

import org.example.gui.frames.AddFrame;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class NonDigitsInNumberExceptionTest {

    @BeforeAll
    static void setUp() { System.out.println("@BeforeAll executed"); }

    @Test
    void nonDigitsInNumberExceptionTest() {

        Throwable thrownFirst = assertThrows(NonDigitsInNumberException.class, () -> AddFrame.publicCheckField("12 32", "int"));
        assertEquals("The number contains an wrong character", thrownFirst.getMessage());

        Throwable thrownSecond = assertThrows(NonDigitsInNumberException.class, () -> AddFrame.publicCheckField("b12", "int"));
        assertEquals("The number contains an wrong character", thrownSecond.getMessage());

        System.out.println("Non DigitsInNumberException executed");
    }

    @AfterAll
    static void tearDown() { System.out.println("@AfterAll executed"); }
}