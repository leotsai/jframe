package org.jframe.core.helpers;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created by Leo on 2017/10/18.
 */
public class XmlHelper {

    public static <T extends Serializable> T deserialize(Class<T> clazz, String xml) throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        T obj = (T) unmarshaller.unmarshal(stream);
        stream.close();
        return obj;
    }

    public static <T extends Serializable> T deserialize(Class<T> clazz, File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        T obj = (T) unmarshaller.unmarshal(xmlFile);
        return obj;
    }

    public static void serializeToFile(Object obj, File file) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, file);
    }

    public static String serialize(Object obj) throws JAXBException, UnsupportedEncodingException, IOException{
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(obj, outputStream);
        String xml = outputStream.toString("UTF-8");
        outputStream.close();
        return xml;
    }

}
