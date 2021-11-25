package com.dissys.bananashop.data;

public class Node {
  private String id;
  private String ip;
  private String port;
  public Node(String id, String ip, String port) {
    this.id = id;
    this.ip = ip;
    if (port == null) {
      this.port = "8080"; // default port
    } else {
      this.port = port;
    }
  }


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPort() {
    return this.port;
  }

  public void setPort(String port) {
    this.port = port;
  }

}
