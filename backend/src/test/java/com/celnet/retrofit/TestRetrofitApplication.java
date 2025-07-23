package com.celnet.retrofit;

import org.springframework.boot.SpringApplication;

public class TestRetrofitApplication {

	public static void main(String[] args) {
		SpringApplication.from(RetrofitApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
