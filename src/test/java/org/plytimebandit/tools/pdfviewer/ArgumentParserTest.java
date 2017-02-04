package org.plytimebandit.tools.pdfviewer;

import org.junit.Assert;
import org.junit.Test;

public class ArgumentParserTest {

    @Test
    public void testEmptyArgs() {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.get("foo");
    }

    @Test
    public void testEmptyArgsGetNull() {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.get(null);
    }

    @Test
    public void testOneArg() {
        ArgumentParser argumentParser = new ArgumentParser("foo");
        String param = argumentParser.get("any");
        Assert.assertEquals("foo", param);
    }

    @Test
    public void testParameterFooAndGetFoo() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "bar");
        String foo = argumentParser.get("foo");
        Assert.assertEquals("bar", foo);
    }

    @Test
    public void testParameterFooAndGetBar() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "bar");
        String foo = argumentParser.get("bar");
        Assert.assertEquals(null, foo);
    }

    @Test
    public void testParameterCastToString() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "foo");
        String foo = argumentParser.get("foo", String.class);
        Assert.assertEquals("foo", foo);
    }

    @Test
    public void testParameterCastToBoolean() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "true");
        Boolean foo = argumentParser.get("foo", Boolean.class);
        Assert.assertEquals(true, foo);
    }

    @Test
    public void testParameterCastToPrimitiveBoolean() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "true");
        boolean foo = argumentParser.get("foo", boolean.class);
        Assert.assertEquals(true, foo);
    }

    @Test
    public void testParameterGetFallsBackToDefault() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "foo");
        String bar = argumentParser.get("bar", "bar");
        Assert.assertEquals("bar", bar);
    }

    @Test
    public void testParameterGetFallsBackToDefaultWithCast() {
        ArgumentParser argumentParser = new ArgumentParser("-foo", "foo");
        boolean bar = argumentParser.get("bar", boolean.class, true);
        Assert.assertEquals(true, bar);
    }
}