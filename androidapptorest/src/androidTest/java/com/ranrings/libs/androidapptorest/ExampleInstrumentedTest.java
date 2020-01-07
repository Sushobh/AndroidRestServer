package com.ranrings.libs.androidapptorest;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleInstrumentedTest {


    @Test
    public void testAdbCommands(){
        try {
          Process process =   Runtime.getRuntime().exec("port foward tcp:8080 tcp:8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
