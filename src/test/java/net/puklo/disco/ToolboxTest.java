package net.puklo.disco;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ToolboxTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_return_tested_string_if_its_not_empty() throws Exception {
        final String testString = "test";
        final String result = Toolbox.notEmpty(testString, "error message you should never see");

        assertEquals(testString, result);
    }

    @Test
    public void should_throw_exception_with_given_message_when_string_is_null() throws Exception {
        final String errorMessage = "null error message";
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(errorMessage);

        Toolbox.notEmpty(null, errorMessage);
    }

    @Test
    public void should_throw_exception_with_given_message_when_string_is_empty() throws Exception {
        final String errorMessage = "empty error message";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(errorMessage);

        Toolbox.notEmpty("", errorMessage);
    }
}