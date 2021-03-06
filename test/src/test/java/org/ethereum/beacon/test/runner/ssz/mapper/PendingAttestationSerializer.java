package org.ethereum.beacon.test.runner.ssz.mapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ethereum.beacon.core.state.PendingAttestation;

public class PendingAttestationSerializer implements ObjectSerializer<PendingAttestation> {
  private com.fasterxml.jackson.databind.ObjectMapper mapper;
  private AttestationDataSerializer attestationDataSerializer;

  public PendingAttestationSerializer(com.fasterxml.jackson.databind.ObjectMapper mapper) {
    this.mapper = mapper;
    this.attestationDataSerializer = new AttestationDataSerializer(mapper);
  }

  @Override
  public Class accepts() {
    return PendingAttestation.class;
  }

  @Override
  public ObjectNode map(PendingAttestation instance) {
    ObjectNode pendingAttestation = mapper.createObjectNode();
    pendingAttestation.put("aggregation_bitfield", instance.getAggregationBits().toString());
    pendingAttestation.set("data", attestationDataSerializer.map(instance.getData()));
    pendingAttestation.set("inclusion_delay", ComparableBigIntegerNode.valueOf(instance.getInclusionDelay()));
    pendingAttestation.set("proposer_index", ComparableBigIntegerNode.valueOf(instance.getProposerIndex()));
    return pendingAttestation;
  }
}
