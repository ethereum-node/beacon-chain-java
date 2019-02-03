package org.ethereum.beacon.core;

import org.ethereum.beacon.core.operations.CustodyChallenge;
import org.ethereum.beacon.core.state.CrosslinkRecord;
import org.ethereum.beacon.core.state.Eth1Data;
import org.ethereum.beacon.core.state.Eth1DataVote;
import org.ethereum.beacon.core.state.ForkData;
import org.ethereum.beacon.core.state.PendingAttestationRecord;
import org.ethereum.beacon.core.state.ValidatorRecord;
import org.ethereum.beacon.core.types.Bitfield64;
import org.ethereum.beacon.core.types.EpochNumber;
import org.ethereum.beacon.core.types.Gwei;
import org.ethereum.beacon.core.types.ShardNumber;
import org.ethereum.beacon.core.types.SlotNumber;
import org.ethereum.beacon.core.types.Time;
import org.ethereum.beacon.core.types.ValidatorIndex;
import tech.pegasys.artemis.ethereum.core.Hash32;
import tech.pegasys.artemis.util.collections.WriteList;
import tech.pegasys.artemis.util.uint.UInt64;

public interface MutableBeaconState extends BeaconState {

  void setSlot(SlotNumber slotNumber);

  void setGenesisTime(Time genesisTime);

  void setForkData(ForkData forkData);

  @Override
  WriteList<ValidatorIndex, ValidatorRecord> getValidatorRegistry();

  @Override
  WriteList<ValidatorIndex, Gwei> getValidatorBalances();

  void setValidatorRegistryLatestChangeSlot(SlotNumber latestChangeSlot);

  void setValidatorRegistryExitCount(UInt64 exitCount);

  void setValidatorRegistryDeltaChainTip(Hash32 deltaChainTip);

  @Override
  WriteList<UInt64, Hash32> getLatestRandaoMixes();

  @Override
  WriteList<Integer, Hash32> getLatestVdfOutputs();

  void setPreviousEpochStartShard(ShardNumber previousEpochStartShard);

  void setCurrentEpochStartShard(ShardNumber currentEpochStartShard);

  void setPreviousEpochCalculationSlot(SlotNumber previousEpochCalculationSlot);

  void setCurrentEpochCalculationSlot(SlotNumber currentEpochCalculationSlot);

  void setPreviousEpochRandaoMix(Hash32 previousEpochRandaoMix);

  void setCurrentEpochRandaoMix(Hash32 currentEpochRandaoMix);

  @Override
  WriteList<Integer, CustodyChallenge> getCustodyChallenges();

  void setPreviousJustifiedSlot(SlotNumber previousJustifiedSlot);

  void setJustifiedSlot(SlotNumber justifiedSlot);

  void setJustificationBitfield(Bitfield64 justificationBitfield);

  void setFinalizedSlot(SlotNumber finalizedSlot);

  @Override
  WriteList<ShardNumber, CrosslinkRecord> getLatestCrosslinks();

  @Override
  WriteList<SlotNumber, Hash32> getLatestBlockRoots();

  @Override
  WriteList<EpochNumber, Gwei> getLatestPenalizedExitBalances();

  @Override
  WriteList<Integer, PendingAttestationRecord> getLatestAttestations();

  @Override
  WriteList<Integer, Hash32> getBatchedBlockRoots();

  void setLatestEth1Data(Eth1Data latestEth1Data);

  @Override
  WriteList<Integer, Eth1DataVote> getEth1DataVotes();

  BeaconState createImmutable();
}
