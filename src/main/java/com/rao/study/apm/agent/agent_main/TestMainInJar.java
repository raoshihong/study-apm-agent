package com.rao.study.apm.agent.agent_main;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class TestMainInJar {
    static class AttachThread extends Thread{
        private final List<VirtualMachineDescriptor> listBefore;

        private final String jar;

        AttachThread(String attachJar, List<VirtualMachineDescriptor> vms) {
            listBefore = vms;  // 记录程序启动时的 VM 集合
            jar = attachJar;
        }

        @Override
        public void run() {
            VirtualMachine vm = null;
            List<VirtualMachineDescriptor> listAfter = null;
            try {
                int count = 0;
                while (true) {
                    listAfter = VirtualMachine.list();
                    for (VirtualMachineDescriptor vmd : listAfter) {
                        if (!listBefore.contains(vmd)) {
                            // 如果 VM 有增加，我们就认为是被监控的 VM 启动了
                            // 这时，我们开始监控这个 VM
                            vm = VirtualMachine.attach(vmd);
                            break;
                        }
                    }
                    Thread.sleep(500);
                    count++;
                    if (null != vm || count >= 10) {
                        break;
                    }
                }
                vm.loadAgent(jar);
                vm.detach();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        new AttachThread("apm-agent-demo-1.0-SNAPSHOT.jar", VirtualMachine.list()).run();

        VirtualMachine attach = VirtualMachine.attach("13008");
        System.out.println(attach.id());
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            System.out.println(vmd);
            if (vmd.displayName().contains("TestMainInJar")) {
                VirtualMachine vm = VirtualMachine.attach(vmd);
                vm.loadAgent("D:\\all-work-space\\idea-workspace\\study-apm-agent\\target\\study-apm-agent-1.0-SNAPSHOT.jar");
                System.out.println("loaded");
                vm.detach();
                System.out.println("detached");
                break;
            }
        }
    }

}