package com.levending.ylcoffeelib.actions;

import com.levending.ylcoffeelib.protocol.d;
import com.levending.ylcoffeelib.protocol.e;
import com.levending.ylcoffeelib.serialport.CoffeeSerialPortManager;

public class TestAction extends com.levending.ylcoffeelib.actions.b {

    private static TestAction g;

    public TestAction() {
    }

    public static TestAction getInstance() {
        if (g == null) {
            g = new TestAction();
        }

        return g;
    }

    public void cleanMachine(byte var1) {
        if (super.f()) {
            super.a(2);
        } else {
            super.e();
            super.a(com.levending.ylcoffeelib.protocol.e.a(CoffeeSerialPortManager.getInstance().spoDriver, var1) ^ true);
        }
    }

}
