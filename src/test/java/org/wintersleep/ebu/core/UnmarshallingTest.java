package org.wintersleep.ebu.core;


import ebu.metadata_schema.ebucore.EbuCoreMainType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.purl.dc.elements._1.ObjectFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static java.lang.String.format;

public class UnmarshallingTest {

    private final static JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(ObjectFactory.class, ebu.metadata_schema.ebucore.ObjectFactory.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testMp2() throws Exception {
        assertExpectedType("mp2");
    }

    @Test
    public void testMp3() throws Exception {
        assertExpectedType("mp3");
    }

    @Test
    public void testMxf() throws Exception {
        assertExpectedType("mxf");
    }

    @Test
    public void testWav() throws Exception {
        assertExpectedType("wav");
    }

    private void assertExpectedType(String wav) throws JAXBException, IOException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        try (InputStream stream = open(wav)) {
            StreamSource source = new StreamSource(stream);
            JAXBElement<EbuCoreMainType> object = unmarshaller.unmarshal(source, EbuCoreMainType.class);
            Assertions.assertEquals(EbuCoreMainType.class, object.getValue().getClass());
        }
    }

    private final InputStream open(String variant) {
        String name = format("test-%s-asset.%s.ebucore.xml", variant, variant);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        return Objects.requireNonNull(stream, "Could not open: " + name);
    }
}
