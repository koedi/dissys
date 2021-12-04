package com.dissys.bananashop.controllers;



import java.util.ArrayList;
import java.util.HashMap;

import com.dissys.bananashop.data.Node;
import com.dissys.bananashop.data.NodeInfo;
import com.dissys.bananashop.model.Bananas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("node-info")
public class NodeInfoController {
  @Autowired
  NodeInfo nodeInfo;

	@GetMapping(path = "/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<Node>> getNodeList() {
		return new ResponseEntity<>(nodeInfo.getNodelist(), HttpStatus.OK);
	}

  @PostMapping(path = "/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postNodeList(@RequestBody ArrayList<Node> newNodeList) {
    nodeInfo.setNodelist(newNodeList);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}