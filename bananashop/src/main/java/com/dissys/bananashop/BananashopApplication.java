package com.dissys.bananashop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BananashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananashopApplication.class, args);
		// initialize state here (get banana count from other node, inform master that this node is present, etc)
	}

}
