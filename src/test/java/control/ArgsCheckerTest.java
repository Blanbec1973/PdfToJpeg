package control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgsCheckerTest {

    @Test
    void testArgsChecker1() {
        ArgsChecker argsChecker = new ArgsChecker(new String[] {"toto"});
        assertEquals("toto",argsChecker.getDirectory());
    }

    @Test
    void testArgsChecker2() {
        ArgsChecker argsChecker = new ArgsChecker(new String[] {""});
        assertEquals(System.getProperty("user.dir"),argsChecker.getDirectory());
    }

}