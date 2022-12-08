package io.getarrays.server;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repository.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 40 Import per CorsFilter


import java.util.Arrays;

// --> ServerResource --> 38 Definiamo un bean di tipo Command Line Runner che verrà eseguito non appena l'applicazione verrà inizializzata e salverà alcuni server
@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	// 38c Ora, con la @Bean, quando avvieremo l'app avremo il seguente contenuto salvato nel database.
	@Bean
	CommandLineRunner run(ServerRepo serverRepo) {
		// 38a returniamo una arrow function, che eseguirà il suo contenuto verrà eseguito subito dopo che l'app verrà inizializzata
		return args -> {
			// 38b importiamo la classe ServerRepo per poter utilizzare la funzione save(), al cui interno inseriamo un nuovo Server
			serverRepo.save(new Server(null, "192.168.1.160", "Ubuntu Linux", "16 GB", "Personal PC", "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
			serverRepo.save(new Server(null, "192.168.1.58", "Fedora Linux", "16 GB", "Dell Tower", "http://localhost:8080/server/image/server2.png", Status.SERVER_DOWN));
			serverRepo.save(new Server(null, "192.168.1.21", "MS 2008", "32 GB", "Web Server", "http://localhost:8080/server/image/server3.png", Status.SERVER_UP));
			serverRepo.save(new Server(null, "192.168.1.14", "Red Hat Enterprise Linux", "64 GB", "Mail Server", "http://localhost:8080/server/image/server4.png", Status.SERVER_DOWN));
		};
	}

	// 38d Ci rimane da configurare la connessione al database --> eliminiamo dalla cartella resources le cartelle static e template: non ne abbiamo bisogno in quanto questa è un'API, quindi non avremo nessun file html. Cambiamo l'estensione file di application.yml in yml --> application.yml

	// 40
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
		config.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-type", "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
		config.setExposedHeaders(Arrays.asList("Origin", "Content-type", "Accept", "Jwt-Token", "Authorization",  "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",  "Access-Control-Allow-Credentials", "Filename"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
