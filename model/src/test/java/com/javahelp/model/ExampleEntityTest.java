package com.javahelp.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example test class for {@link ExampleEntity}
 */
public class ExampleEntityTest {

    @Test
    public void exampleEntity_works() {
        assertEquals("name", new ExampleEntity("name").name);
    }

}
