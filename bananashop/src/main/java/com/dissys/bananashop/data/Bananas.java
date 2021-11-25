package com.dissys.bananashop.data;

import org.springframework.stereotype.Component;
@Component
public class Bananas {

  
  private int amount;

  public Bananas() {
    this.amount = -1;
  }

  public int getAmount() {
    return this.amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

}
