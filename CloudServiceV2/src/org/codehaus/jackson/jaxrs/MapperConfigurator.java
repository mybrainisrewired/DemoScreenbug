package org.codehaus.jackson.jaxrs;

import com.wmt.data.LocalAudioAll;
import java.util.ArrayList;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class MapperConfigurator {
    protected Annotations[] _defaultAnnotationsToUse;
    protected ObjectMapper _defaultMapper;
    protected Class<? extends AnnotationIntrospector> _jaxbIntrospectorClass;
    protected ObjectMapper _mapper;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$jaxrs$Annotations;

        static {
            $SwitchMap$org$codehaus$jackson$jaxrs$Annotations = new int[Annotations.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$jaxrs$Annotations[Annotations.JACKSON.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$org$codehaus$jackson$jaxrs$Annotations[Annotations.JAXB.ordinal()] = 2;
        }
    }

    public MapperConfigurator(ObjectMapper mapper, Annotations[] defAnnotations) {
        this._mapper = mapper;
        this._defaultAnnotationsToUse = defAnnotations;
    }

    protected AnnotationIntrospector _resolveIntrospector(Annotations ann) {
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$jaxrs$Annotations[ann.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return new JacksonAnnotationIntrospector();
            case ClassWriter.COMPUTE_FRAMES:
                try {
                    if (this._jaxbIntrospectorClass == null) {
                        this._jaxbIntrospectorClass = JaxbAnnotationIntrospector.class;
                    }
                    return (AnnotationIntrospector) this._jaxbIntrospectorClass.newInstance();
                } catch (Exception e) {
                    Exception e2 = e;
                    throw new IllegalStateException("Failed to instantiate JaxbAnnotationIntrospector: " + e2.getMessage(), e2);
                }
            default:
                throw new IllegalStateException();
        }
    }

    protected AnnotationIntrospector _resolveIntrospectors(Annotations[] annotationsToUse) {
        ArrayList<AnnotationIntrospector> intr = new ArrayList();
        Annotations[] arr$ = annotationsToUse;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Annotations a = arr$[i$];
            if (a != null) {
                intr.add(_resolveIntrospector(a));
            }
            i$++;
        }
        if (intr.size() == 0) {
            return AnnotationIntrospector.nopInstance();
        }
        AnnotationIntrospector curr = (AnnotationIntrospector) intr.get(0);
        int i = 1;
        int len = intr.size();
        while (i < len) {
            curr = AnnotationIntrospector.pair(curr, (AnnotationIntrospector) intr.get(i));
            i++;
        }
        return curr;
    }

    protected void _setAnnotations(ObjectMapper mapper, Annotations[] annotationsToUse) {
        AnnotationIntrospector intr;
        if (annotationsToUse == null || annotationsToUse.length == 0) {
            intr = AnnotationIntrospector.nopInstance();
        } else {
            intr = _resolveIntrospectors(annotationsToUse);
        }
        mapper.getDeserializationConfig().setAnnotationIntrospector(intr);
        mapper.getSerializationConfig().setAnnotationIntrospector(intr);
    }

    public synchronized void configure(Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(JsonParser.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(DeserializationConfig.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(SerializationConfig.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized ObjectMapper getConfiguredMapper() {
        return this._mapper;
    }

    public synchronized ObjectMapper getDefaultMapper() {
        if (this._defaultMapper == null) {
            this._defaultMapper = new ObjectMapper();
            _setAnnotations(this._defaultMapper, this._defaultAnnotationsToUse);
        }
        return this._defaultMapper;
    }

    protected ObjectMapper mapper() {
        if (this._mapper == null) {
            this._mapper = new ObjectMapper();
            _setAnnotations(this._mapper, this._defaultAnnotationsToUse);
        }
        return this._mapper;
    }

    public synchronized void setAnnotationsToUse(Annotations[] annotationsToUse) {
        _setAnnotations(mapper(), annotationsToUse);
    }

    public synchronized void setMapper(ObjectMapper m) {
        this._mapper = m;
    }
}