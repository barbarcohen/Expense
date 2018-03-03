package cz.rudypokorny.expense;

import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.JLineShellComponent;

public class ShellBootstrap {

    public static void main(String []args){
        Bootstrap bootstrap = new Bootstrap();
        JLineShellComponent shell = bootstrap.getJLineShellComponent();
        shell.executeCommand("help");
    }
}
