package org.ethereum.beacon.test.type.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ethereum.beacon.test.type.DataMapperTestCase;
import org.ethereum.beacon.test.type.state.field.BlocksField;
import org.ethereum.beacon.test.type.state.field.BlsSettingField;
import org.ethereum.beacon.test.type.state.field.PostField;
import org.ethereum.beacon.test.type.state.field.PreField;
import tech.pegasys.artemis.util.bytes.BytesValue;

import java.util.Map;

public class SanityBlocksCase extends DataMapperTestCase
    implements BlsSettingField, PreField, PostField, BlocksField {
  public SanityBlocksCase(
      Map<String, BytesValue> files, ObjectMapper objectMapper, String description) {
    super(files, objectMapper, description);
  }

  @Override
  public String toString() {
    return "SanityBlocksCase{" + super.toString() + '}';
  }
}
