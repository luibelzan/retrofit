package com.celnet.retrofit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RetrofitApplicationTests {

	@Test
	void contextLoads() {
	}

}
