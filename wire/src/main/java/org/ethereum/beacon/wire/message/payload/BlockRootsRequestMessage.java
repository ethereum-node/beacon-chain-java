package org.ethereum.beacon.wire.message.payload;

import org.ethereum.beacon.core.types.SlotNumber;
import org.ethereum.beacon.ssz.annotation.SSZ;
import org.ethereum.beacon.ssz.annotation.SSZSerializable;
import org.ethereum.beacon.wire.message.RequestMessagePayload;
import tech.pegasys.artemis.util.uint.UInt64;

@SSZSerializable
public class BlockRootsRequestMessage extends RequestMessagePayload {
  public static final int METHOD_ID = 0x0F;

  @SSZ private final SlotNumber startSlot;
  @SSZ private final UInt64 count;

  public BlockRootsRequestMessage(SlotNumber startSlot, UInt64 count) {
    this.startSlot = startSlot;
    this.count = count;
  }

  @Override
  public int getMethodId() {
    return METHOD_ID;
  }

  public SlotNumber getStartSlot() {
    return startSlot;
  }

  public UInt64 getCount() {
    return count;
  }

  @Override
  public String toString() {
    return "BlockRootsRequestMessage{" +
        "startSlot=" + startSlot +
        ", count=" + count +
        '}';
  }
}
