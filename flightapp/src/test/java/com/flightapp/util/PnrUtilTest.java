package com.flightapp.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PnrUtilTest {

    @Test
    void testGenerateLength() {
        String p = PnrUtil.generate();
        assertNotNull(p);
        assertTrue(p.length() >= 8 && p.length() <= 12); // your PnrUtil uses 10 chars; be flexible
    }

    @Test
    void testGenerateUniqueness() {
        Set<String> s = IntStream.range(0,1000).mapToObj(i->PnrUtil.generate()).collect(Collectors.toSet());
        assertEquals(1000, s.size()); // extremely likely to be unique for 1000 samples
    }
}
