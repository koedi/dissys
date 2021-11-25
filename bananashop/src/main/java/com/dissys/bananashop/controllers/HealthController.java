package com.dissys.bananashop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/ping")
	public ResponseEntity<?> ping() {
		return new ResponseEntity<>("pong", HttpStatus.OK);
	}

}