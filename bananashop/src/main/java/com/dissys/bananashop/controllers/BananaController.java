package com.dissys.bananashop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissys.bananashop.data.NodeInfo;
import com.dissys.bananashop.model.Bananas;
import com.dissys.bananashop.model.Order;

@RestController
@RequestMapping("api/")
public class BananaController {
  @Autowired
  Bananas b;
  @Autowired
  NodeInfo nodeInfo;
  
	@GetMapping(path = "bananas/")
	public long getBananas() {
		return b.getAmount();
	}

  @PostMapping(path = "bananas/")
	public Order postBananas(@RequestBody Order order) {
	  	long newValue = b.getAmount() - order.getItemsPurchased();
    	b.setAmount(newValue);
    	order.setItemsAvailable(b.getAmount());
		return order;
	}

}
