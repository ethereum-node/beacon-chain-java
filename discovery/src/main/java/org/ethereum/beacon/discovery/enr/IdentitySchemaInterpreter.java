package org.ethereum.beacon.discovery.enr;

import tech.pegasys.artemis.util.bytes.Bytes32;

/**
 * Interprets identity schema of ethereum node record:
 *
 * <ul>
 *   <li>derives node id from node record
 *   <li>>signs node record
 *   <li>verifies signature of node record
 * </ul>
 */
public interface IdentitySchemaInterpreter {
  /** Returns supported scheme */
  IdentitySchema getScheme();

  /* Signs nodeRecord, modifying it */
  void sign(NodeRecord nodeRecord, Object signOptions);

  /** Verifies that `nodeRecord` is of scheme implementation */
  default void verify(NodeRecord nodeRecord) {
    if (!nodeRecord.getIdentityScheme().equals(getScheme())) {
      throw new RuntimeException("Interpreter and node record schemes do not match!");
    }
  }

  /** Delivers nodeId according to identity scheme scheme */
  Bytes32 getNodeId(NodeRecord nodeRecord);
}
