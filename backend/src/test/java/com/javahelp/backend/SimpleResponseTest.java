package com.javahelp.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleResponseTest {

    @Test
    public void testSimpleResponse() {
        String expectedMessage = "Test Message";
        SimpleResponse response = new SimpleResponse(expectedMessage);
        assertEquals(expectedMessage, response.getMessage());
    }

}
