package com.dissys.bananashop.model;

import org.springframework.stereotype.Component;
@Component
public class Bananas {

  private long amount;

  public Bananas() {
    this.amount = 100;
  }

  public long getAmount() {
    return this.amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

}
