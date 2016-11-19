/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufes.llcostalonga.android.rateurmate.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestRateUrMateContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_PRESENTATION_ASSESSEMENT = "/1419033600/1111";
    private static final long TEST_ASSESSEMENT= 1419033600L;  // December 20th, 2014

    /*
        Students: Uncomment this out to test your weather location function.
     */
    public void testBuildAssessementUri() {
        Uri assessementUri = RateUrMateContract.AssessementEntry.buildAssessementUri(TEST_ASSESSEMENT);


        assertNotNull("Error: Null Uri returned.  You must fill-in buildAssessementUri in " +
                        "RateURmate Contract.",
                        assessementUri);

        assertEquals("Error:  Uri doesn't match our expected result.",
                assessementUri.toString(),
                "content://br.ufes.llcostalonga.android.rateurmate.app/assessement/1419033600");

        assertEquals("Error: Assessement not properly appended to the end of the Uri.",
                Long.toString(TEST_ASSESSEMENT), assessementUri.getLastPathSegment());



    }
}
