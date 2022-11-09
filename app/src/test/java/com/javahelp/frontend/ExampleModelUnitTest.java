package com.javahelp.frontend;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.ExampleEntity;

import org.junit.Test;

/**
 * Example test suite testing elements from the model module within the app module
 */
public class ExampleModelUnitTest {

    @Test
    public void exampleEntity_works() {
        assertEquals("model in app", new ExampleEntity("model in app").name);
    }

}
