package info.loenwind.mves.impl;

public class SimpleEnergyBuffer {

  private final long maxAmount;
  private long amount = 0;

  public SimpleEnergyBuffer(long maxAmount) {
    this.maxAmount = maxAmount;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    if (amount <= maxAmount) {
      this.amount = amount;
    } else {
      this.amount = maxAmount;
    }
  }

  public long getMaxAmount() {
    return maxAmount;
  }

}
