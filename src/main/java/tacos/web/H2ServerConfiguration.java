/*package tacos.web;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.net.ServerSocket;

@Configuration
public class H2ServerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        int h2Port = findAvailablePort();
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", String.valueOf(h2Port)).start();
    }

    private int findAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            throw new RuntimeException("Could not find an available port for H2 server.", e);
        }
    }
}
*/