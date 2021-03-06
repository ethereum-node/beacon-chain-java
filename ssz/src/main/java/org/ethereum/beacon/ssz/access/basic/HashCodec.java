package org.ethereum.beacon.ssz.access.basic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.consensys.cava.bytes.Bytes;
import org.ethereum.beacon.ssz.visitor.SSZReader;
import org.ethereum.beacon.ssz.visitor.SSZWriter;
import net.consensys.cava.ssz.SSZException;
import org.ethereum.beacon.ssz.access.SSZField;
import org.ethereum.beacon.ssz.SSZSchemeException;
import org.ethereum.beacon.ssz.access.SSZBasicAccessor;
import tech.pegasys.artemis.ethereum.core.Hash32;
import tech.pegasys.artemis.util.bytes.Bytes32;
import tech.pegasys.artemis.util.bytes.BytesValue;

/**
 * SSZ Codec designed to work with fixed size bytes data classes representing hashes, check list in
 * {@link #getSupportedClasses()}
 */
public class HashCodec implements SSZBasicAccessor {

  private static Set<String> supportedTypes = new HashSet<>();

  private static Set<Class> supportedClassTypes = new HashSet<>();

  static {
    supportedClassTypes.add(Hash32.class);
  }

  private static Bytes[] repackBytesList(List<BytesValue> list) {
    Bytes[] data = new Bytes[list.size()];
    for (int i = 0; i < list.size(); i++) {
      byte[] el = list.get(i).getArrayUnsafe();
      data[i] = Bytes.of(el);
    }

    return data;
  }

  @Override
  public Set<String> getSupportedSSZTypes() {
    return supportedTypes;
  }

  @Override
  public Set<Class> getSupportedClasses() {
    return supportedClassTypes;
  }

  @Override
  public int getSize(SSZField field) {
    return 32;
  }

  @Override
  public void encode(Object value, SSZField field, OutputStream result) {
    Bytes res = null;
    BytesValue data = (BytesValue) value;
    res = SSZWriter.encodeBytes(Bytes.of(data.getArrayUnsafe()), getSize(field));

    try {
      result.write(res.toArrayUnsafe());
    } catch (IOException e) {
      String error = String.format("Failed to write data of type %s to stream",
          field.getRawClass());
      throw new SSZException(error, e);
    }
  }

  @Override
  public Object decode(SSZField field, SSZReader reader) {
    HashType hashType = parseFieldType(field);

    try {
      return Hash32.wrap(Bytes32.wrap(reader.readHash(hashType.size).toArrayUnsafe()));
    } catch (Exception ex) {
      String error = String.format("Failed to read data from stream to field \"%s\"",
          field.getName());
      throw new SSZException(error, ex);
    }
  }

  private HashType parseFieldType(SSZField field) {
    if (field.getRawClass().equals(Hash32.class)) {
      return HashType.of(32);
    }

    throw new SSZSchemeException(String.format("Hash of class %s is not supported",
        field.getRawClass()));
  }

  static class HashType {
    final int size;

    HashType(int size) {
      this.size = size;
    }

    static HashType of(int size) {
      HashType res = new HashType(size);
      return res;
    }
  }
}
