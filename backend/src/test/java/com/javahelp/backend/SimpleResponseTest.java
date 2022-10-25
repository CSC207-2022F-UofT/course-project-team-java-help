package com.javahelp.backend;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimpleResponseTest {

    @Test
    public void testSimpleResponse() {
        String expectedMessage = "Test Message";
        SimpleResponse response = new SimpleResponse(expectedMessage);
        assertEquals(expectedMessage, response.getMessage());
    }

}
