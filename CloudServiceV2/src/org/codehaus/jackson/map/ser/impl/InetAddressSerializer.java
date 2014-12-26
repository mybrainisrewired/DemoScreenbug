package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.net.InetAddress;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class InetAddressSerializer extends ScalarSerializerBase<InetAddress> {
    public static final InetAddressSerializer instance;

    static {
        instance = new InetAddressSerializer();
    }

    public InetAddressSerializer() {
        super(InetAddress.class);
    }

    public void serialize(InetAddress value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        String str = value.toString().trim();
        int ix = str.indexOf(Opcodes.V1_3);
        if (ix >= 0) {
            if (ix == 0) {
                str = str.substring(1);
            } else {
                str = str.substring(0, ix);
            }
        }
        jgen.writeString(str);
    }

    public void serializeWithType(InetAddress value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForScalar(value, jgen, InetAddress.class);
        serialize(value, jgen, provider);
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }
}