package org.example.errors;

import org.example.gui.frames.AddFrame;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EmptyFieldExceptionTest {

    @BeforeAll
    static void setUp() {
        System.out.println("@BeforeAll executed");
    }

    @Test
    void emptyFieldExceptionTest() {
        Throwable thrownFirst = assertThrows(EmptyFieldException.class, () -> AddFrame.publicCheckField("", "str"));
        assertEquals("Not all fields are filled or first symbol is NULL", thrownFirst.getMessage());

        Throwable thrownSecond = assertThrows(EmptyFieldException.class, () -> AddFrame.publicCheckField(" b ", "str"));
        assertEquals("Not all fields are filled or first symbol is NULL", thrownSecond.getMessage());

        System.out.println("EmptyFieldException executed");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("@AfterAll executed");
    }
}