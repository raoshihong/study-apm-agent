package com.rao.study.apm.agent.agent_main;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class TestMainInJar {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(new TransClass().getNumber());
        while (true) {
            Thread.sleep(2000);
            int number = new TransClass().getNumber();
            System.out.println(number);
        }
    }

}
