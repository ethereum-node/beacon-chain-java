package org.ethereum.beacon.consensus.verifier.block;

import org.ethereum.beacon.consensus.BeaconChainSpec;
import org.ethereum.beacon.consensus.verifier.BeaconBlockVerifier;
import org.ethereum.beacon.consensus.verifier.VerificationResult;
import org.ethereum.beacon.core.BeaconBlock;
import org.ethereum.beacon.core.BeaconState;
import org.ethereum.beacon.core.envelops.SignedBeaconBlock;
import org.ethereum.beacon.core.types.BLSSignature;

/**
 * Verifies block header.
 *
 * @see <a
 *     href="https://github.com/ethereum/eth2.0-specs/blob/v0.9.2/specs/core/0_beacon-chain.md#block-header">Block
 *     header</a> in the spec.
 */
public class BlockHeaderVerifier implements BeaconBlockVerifier {

  private BeaconChainSpec spec;

  public BlockHeaderVerifier(BeaconChainSpec spec) {
    this.spec = spec;
  }

  @Override
  public VerificationResult verify(SignedBeaconBlock block, BeaconState state) {
    try {
      if (!block.getSignature().equals(BLSSignature.ZERO)) {
        spec.verify_block_signature(state, block);
      }
      spec.verify_block_header(state, block.getMessage());
      return VerificationResult.PASSED;
    } catch (Exception e) {
      return VerificationResult.failedResult(
          "Block header verification has failed: %s", e.getMessage());
    }
  }
}
