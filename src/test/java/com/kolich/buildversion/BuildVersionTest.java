/*
 * Copyright (c) 2026 Mark S. Kolich
 * https://mark.koli.ch
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kolich.buildversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BuildVersionTest {

    @Test
    public void readsTheApplicationsBuildProperties() {
        final BuildVersion buildVersion = BuildVersion.getInstance();

        assertEquals("abc1234", buildVersion.getBuildNumber());
        assertEquals("2026-10-13T09:15:00-07:00", buildVersion.getTimestamp());
        assertEquals("1.2.3", buildVersion.getVersion());
        assertEquals("main", buildVersion.getBranch());
        assertSame(buildVersion, BuildVersion.getInstance());
    }

    @Test
    public void missingPropertiesAreUnknown() {
        final BuildVersion buildVersion = new BuildVersion("empty-build-version.properties");

        assertEquals("unknown", buildVersion.getBuildNumber());
        assertEquals("unknown", buildVersion.getTimestamp());
        assertEquals("unknown", buildVersion.getVersion());
        assertEquals("unknown", buildVersion.getBranch());
    }

    @Test
    public void missingPropertiesFileFails() {
        assertThrows(IllegalStateException.class, () -> new BuildVersion("does-not-exist.properties"));
    }

}
