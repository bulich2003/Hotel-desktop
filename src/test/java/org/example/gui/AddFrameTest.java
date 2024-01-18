package org.example.gui;

import org.example.gui.frames.AddFrame;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AddFrameTest {
    @BeforeAll
    static void setUp() {
        System.out.println("@BeforeAll executed");
    }

    @Test
    void isOnlyDigitsTest() {
        assertTrue(AddFrame.publicIsNumber("2"));
        assertTrue(AddFrame.publicIsNumber("123"));
        System.out.println("Test isNumber executed");
    }

    @Test
    void isNonOnlyDigitsTest() {
        assertFalse(AddFrame.publicIsNumber(""));
        assertFalse(AddFrame.publicIsNumber("123n43"));
        assertFalse(AddFrame.publicIsNumber("12 34"));
        assertFalse(AddFrame.publicIsNumber(" "));
        System.out.println("Test isNonNumber executed");
    }

    @Test
    void checkFieldTest() {

        Throwable thrownFirst = assertThrows(Exception.class, () -> AddFrame.publicCheckField("", "str"));
        assertNotNull(thrownFirst.getMessage());

        Throwable thrownSecond = assertThrows(Exception.class, () -> AddFrame.publicCheckField("   ", "str"));
        assertNotNull(thrownSecond.getMessage());

        Throwable thrownThird = assertThrows(Exception.class, () -> AddFrame.publicCheckField("0123", "int"));
        assertNotNull(thrownThird.getMessage());

        Throwable thrownFourth = assertThrows(Exception.class, () -> AddFrame.publicCheckField("14 345b", "int"));
        assertNotNull(thrownFourth.getMessage());

        System.out.println("Test checkField executed");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("@AfterAll executed");
    }
}