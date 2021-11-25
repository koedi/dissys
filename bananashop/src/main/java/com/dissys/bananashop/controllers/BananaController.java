package com.dissys.bananashop.controllers;

import com.dissys.bananashop.data.Bananas;
import com.dissys.bananashop.data.NodeInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bananas")
public class BananaController {
  @Autowired
  Bananas b;
  @Autowired
  NodeInfo nodeInfo;
  
	@GetMapping(path = "", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Bananas> getBananas() {
		return new ResponseEntity<Bananas>(b, HttpStatus.OK);
	}

  @PostMapping(path = "/order", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postBananas(@RequestBody int amount) {
    int newValue = b.getAmount() - amount;
    if (newValue < 0) {
      return new ResponseEntity<>("Not enough bananas", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // inform other nodes about the order
    b.setAmount(newValue);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
