package com.epsagon;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class Installer {
    public static synchronized void install() {
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();

        for (VirtualMachineDescriptor vmd : vms) {
            try {
                VirtualMachine vm = VirtualMachine.attach(vmd.id());
                try {
                    vm.loadAgent("/var/task/agent.jar");
                } finally {
                    vm.detach();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Error attaching to VM, skipping instrumentation");
            }
        }
    }
}
