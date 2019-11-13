package com.simd.pvp;

import com.simd.pvp.netty.GameServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PvPApplication {
	private static final String PROPERTIES =
			"spring.config.location="
					+"classpath:/application.yml"
					+",classpath:/db.yml";
	public static void main(String[] args) {

		SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(PvPApplication.class);
		springApplicationBuilder.properties(PROPERTIES).run(args);

		GameServer gameServer = springApplicationBuilder.context().getBean(GameServer.class);
		gameServer.runGameServer();

	}


}
