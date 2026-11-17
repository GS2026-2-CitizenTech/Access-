package br.com.fiap.accessplus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AccessplusApplicationTests {

	@Test
	void applicationClassIsAvailable() {
		assertDoesNotThrow(() -> AccessplusApplication.class.getDeclaredMethod("main", String[].class));
	}

}
