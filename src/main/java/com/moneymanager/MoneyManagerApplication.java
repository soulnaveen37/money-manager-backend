package com.moneymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Flapdoodle embedded Mongo imports
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.mongo.distribution.Version;

@SpringBootApplication
public class MoneyManagerApplication {

    private static MongodExecutable mongodExecutable;

    public static void main(String[] args) throws Exception {
        // Start embedded MongoDB on default port 27017 for development if possible
        try {
            MongodStarter starter = MongodStarter.getDefaultInstance();
            int port = 27017;
            MongodConfig mongodConfig = MongodConfig.builder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(port, Network.localhostIsIPv6()))
                    .build();

            mongodExecutable = starter.prepare(mongodConfig);
            mongodExecutable.start();
            System.out.println("Embedded MongoDB started on port " + port);
        } catch (Exception e) {
            // If embedded Mongo fails to start, log and continue; application may connect to external Mongo
            System.err.println("Failed to start embedded MongoDB: " + e.getMessage());
        }

        SpringApplication.run(MoneyManagerApplication.class, args);
    }
}
