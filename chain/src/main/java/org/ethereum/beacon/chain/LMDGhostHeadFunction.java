package org.ethereum.beacon.chain;

import org.ethereum.beacon.chain.storage.BeaconBlockStorage;
import org.ethereum.beacon.chain.storage.BeaconChainStorage;
import org.ethereum.beacon.chain.storage.BeaconStateStorage;
import org.ethereum.beacon.consensus.HeadFunction;
import org.ethereum.beacon.consensus.SpecHelpers;
import org.ethereum.beacon.core.BeaconBlock;
import org.ethereum.beacon.core.BeaconState;
import org.ethereum.beacon.core.operations.Attestation;
import org.ethereum.beacon.core.state.ValidatorRecord;
import tech.pegasys.artemis.ethereum.core.Hash32;
import java.util.List;
import java.util.function.Function;

/**
 * The beacon chain fork choice rule is a hybrid that combines justification and finality with
 * Latest Message Driven (LMD) Greediest Heaviest Observed SubTree (GHOST). For more info check <a
 * href="https://github.com/ethereum/eth2.0-specs/blob/master/specs/core/0_beacon-chain.md#beacon-chain-fork-choice-rule">Beacon
 * chain fork choice rule</a>
 */
public class LMDGhostHeadFunction implements HeadFunction {

  private final BeaconBlockStorage blockStorage;
  private final BeaconStateStorage stateStorage;
  private final SpecHelpers specHelpers;
  Function<ValidatorRecord, Attestation> latestAttestationStorage;
  private final int SEARCH_LIMIT = Integer.MAX_VALUE;

  public LMDGhostHeadFunction(
      BeaconChainStorage chainStorage,
      Function<ValidatorRecord, Attestation> latestAttestationStorage,
      SpecHelpers specHelpers) {
    this.stateStorage = chainStorage.getBeaconStateStorage();
    this.blockStorage = chainStorage.getBeaconBlockStorage();
    this.latestAttestationStorage = latestAttestationStorage;
    this.specHelpers = specHelpers;
  }

  @Override
  public BeaconBlock getHead() {
    BeaconBlock justifiedBlock =
        blockStorage
            .getJustifiedBlock(blockStorage.getMaxSlot(), SEARCH_LIMIT)
            .orElseThrow(() -> new RuntimeException("Couldn't find any justified block"));
    BeaconState justifiedState =
        stateStorage
            .get(justifiedBlock.getHash())
            .orElseThrow(() -> new IllegalStateException("State not found for existing head"));
    Function<Hash32, List<BeaconBlock>> getChildrenBlocks =
        (hash) -> blockStorage.getChildren(hash, SEARCH_LIMIT);
    BeaconBlock newHead =
        specHelpers.lmd_ghost(
            justifiedBlock,
            justifiedState,
            blockStorage::get,
            getChildrenBlocks,
            this::get_latest_attestation);

    return newHead;
  }

  /**
   * Let get_latest_attestation(store, validator) be the attestation with the highest slot number in
   * store from validator. If several such attestations exist, use the one the validator v observed
   * first.
   */
  private Attestation get_latest_attestation(ValidatorRecord validatorRecord) {
    return latestAttestationStorage.apply(validatorRecord);
  }
}