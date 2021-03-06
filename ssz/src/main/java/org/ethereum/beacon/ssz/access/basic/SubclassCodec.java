package org.ethereum.beacon.ssz.access.basic;

import java.io.OutputStream;
import java.util.Set;

import org.ethereum.beacon.ssz.visitor.SSZReader;
import org.ethereum.beacon.ssz.access.SSZBasicAccessor;
import org.ethereum.beacon.ssz.creator.ConstructorObjCreator;
import org.ethereum.beacon.ssz.access.SSZField;
import org.ethereum.beacon.ssz.annotation.SSZSerializable;

/**
 * The SSZBasicAccessor which implements logic of {@link SSZSerializable#serializeAs()} attribute
 * It delegates calls to wrapped Codec corresponding to <code>serializeAs</code> class
 * but substitutes <code>field.type</code> with the <code>serializeAs</code> class
 * and decodes result to the original <code>field.type</code>.
 */
public class SubclassCodec implements SSZBasicAccessor {

  private final SSZBasicAccessor superclassCodec;

  public SubclassCodec(SSZBasicAccessor superclassCodec) {
    this.superclassCodec = superclassCodec;
  }

  @Override
  public Set<String> getSupportedSSZTypes() {
    return superclassCodec.getSupportedSSZTypes();
  }

  @Override
  public Set<Class> getSupportedClasses() {
    return superclassCodec.getSupportedClasses();
  }

  @Override
  public int getSize(SSZField field) {
    return superclassCodec.getSize(getSerializableField(field));
  }

  @Override
  public void encode(Object value, SSZField field,
      OutputStream result) {
    superclassCodec.encode(value, getSerializableField(field), result);
  }

  @Override
  public Object decode(SSZField field, SSZReader reader) {
    SSZField serializableField = getSerializableField(field);
    Object serializableTypeObject = superclassCodec.decode(serializableField, reader);
    return ConstructorObjCreator.createInstanceWithConstructor(
        field.getRawClass(), new Class[] {serializableField.getRawClass()}, new Object[] {serializableTypeObject});
  }

  private static SSZField getSerializableField(SSZField field) {
    return new SSZField(getSerializableClass(field.getRawClass()),
    field.getFieldAnnotation(),
    field.getExtraType(),
    field.getExtraSize(),
    field.getName(),
    field.getGetter());
  }

  /**
   *  If the field class specifies {@link SSZSerializable#serializeAs()} attribute
   *  returns the specified class.
   *  Else returns type value.
   */
  public static Class<?> getSerializableClass(Class<?> type) {
    SSZSerializable fieldClassAnnotation = type.getAnnotation(SSZSerializable.class);
    if (fieldClassAnnotation != null && fieldClassAnnotation.serializeAs() != void.class) {
      // the class of the field wants to be serialized as another class
      return fieldClassAnnotation.serializeAs();
    } else {
      return type;
    }
  }


}
