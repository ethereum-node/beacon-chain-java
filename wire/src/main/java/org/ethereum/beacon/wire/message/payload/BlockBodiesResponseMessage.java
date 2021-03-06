package org.ethereum.beacon.wire.message.payload;

import java.util.List;
import org.ethereum.beacon.core.BeaconBlockBody;
import org.ethereum.beacon.ssz.annotation.SSZ;
import org.ethereum.beacon.ssz.annotation.SSZSerializable;
import org.ethereum.beacon.wire.message.ResponseMessagePayload;

@SSZSerializable
public class BlockBodiesResponseMessage extends ResponseMessagePayload {

  @SSZ private final List<BeaconBlockBody> blockBodies;

  public BlockBodiesResponseMessage(List<BeaconBlockBody> blockBodies) {
    this.blockBodies = blockBodies;
  }

  public List<BeaconBlockBody> getBlockBodies() {
    return blockBodies;
  }

  @Override
  public String toString() {
    return "BlockBodiesResponseMessage{" +
        "blockBodies=" + blockBodies +
        '}';
  }
}
