package com.codesolution.projectmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ProjectmanagementApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
		// Vérifie que la méthode main ne lance pas d'exception
		assertDoesNotThrow(() -> ProjectmanagementApplication.main(new String[] {}));
	}

}
