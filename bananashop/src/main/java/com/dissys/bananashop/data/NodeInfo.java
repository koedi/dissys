package com.dissys.bananashop.data;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
@Component
public class NodeInfo {

  private String id;
  private ArrayList<Node> nodelist; // node-id -> ip:port


  public NodeInfo() {
    this.id = "-1";
    this.nodelist = new ArrayList<>();
  }


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public ArrayList<Node> getNodelist() {
    return this.nodelist;
  }

  public void setNodelist(ArrayList<Node> nodelist) {
    this.nodelist = nodelist;
  }
  


  
}