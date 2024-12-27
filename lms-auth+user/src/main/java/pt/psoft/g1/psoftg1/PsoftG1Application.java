package pt.psoft.g1.psoftg1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import pt.psoft.g1.psoftg1.usermanagement.services.DatabaseSyncClient;

@SpringBootApplication
public class PsoftG1Application {

	@Autowired
	private DatabaseSyncClient databaseSyncClient;

	public static void main(String[] args) {
		SpringApplication.run(PsoftG1Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		// Trigger database sync when the application starts
		databaseSyncClient.syncDatabase();
	}

}
