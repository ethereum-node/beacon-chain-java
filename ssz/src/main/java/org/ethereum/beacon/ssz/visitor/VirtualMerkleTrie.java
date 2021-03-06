package org.ethereum.beacon.ssz.visitor;

import tech.pegasys.artemis.ethereum.core.Hash32;
import tech.pegasys.artemis.util.bytes.Bytes32;
import tech.pegasys.artemis.util.bytes.BytesValue;

/** Replaces root of {@link MerkleTrie}, replicating interface */
public class VirtualMerkleTrie extends MerkleTrie {
  private Hash32 root;

  public VirtualMerkleTrie(BytesValue[] nodes, BytesValue root) {
    super(nodes);
    this.root = Hash32.wrap(Bytes32.leftPad(root));
  }

  @Override
  public Hash32 getPureRoot() {
    return root;
  }

  @Override
  public Hash32 getFinalRoot() {
    return root;
  }

  @Override
  public void setFinalRoot(Hash32 finalRoot) {
    this.root = finalRoot;
  }

  @Override
  public VirtualMerkleTrie copy() {
    return new VirtualMerkleTrie(super.nodes, root.copy());
  }
}
