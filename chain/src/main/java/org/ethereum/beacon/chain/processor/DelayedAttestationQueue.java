package org.ethereum.beacon.chain.processor;

import org.ethereum.beacon.core.operations.Attestation;
import org.ethereum.beacon.core.types.SlotNumber;

public interface DelayedAttestationQueue {

  void onTick(SlotNumber slot);

  void onAttestation(Attestation attestation);
}