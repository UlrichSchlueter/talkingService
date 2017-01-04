package com.ulrichschlueter.talkingService;

import io.dropwizard.lifecycle.ServerLifecycleListener;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by uli on 03.01.17.
 */
public class RuntimeInfo implements ServerLifecycleListener {

    private static final String APPLICATION_CONNECTOR = "application";
    private static final String ADMIN_CONNECTOR = "admin";
    private ConsulHealthReporter consulHealthReporter;
    private int applicationPort;
    private int adminPort;

    Logger log = LoggerFactory.getLogger(RuntimeInfo.class);

    public RuntimeInfo(ConsulHealthReporter consulHealthReporter) {
        this.consulHealthReporter = consulHealthReporter;
    }


    public int getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(int applicationPort) {
        this.applicationPort = applicationPort;
    }

    public int getAdminPort() {
        return adminPort;
    }

    public void setAdminPort(int adminPort) {
        this.adminPort = adminPort;
    }


    @Override
    public void serverStarted(Server server) {
        for (final Connector connector : server.getConnectors()) {
            try {
                final ServerSocketChannel channel = (ServerSocketChannel) connector
                        .getTransport();
                final InetSocketAddress socket = (InetSocketAddress) channel
                        .getLocalAddress();

                String s = connector.getName();
                if (s.equals(APPLICATION_CONNECTOR)) {
                    applicationPort = socket.getPort();
                } else if (s.equals(ADMIN_CONNECTOR)) {
                    adminPort = socket.getPort();
                }
            } catch (Exception e) {
            }


        }
        log.info("Application port:" + applicationPort );
        log.info("Admin port:" + adminPort );
        if (consulHealthReporter != null)
        {
            consulHealthReporter.registerAndStartTimer(applicationPort);

        }
    }


}
