package org.jframe.core.weixin.core.helpers;

import java.io.Serializable;

/**
 * Created by Leo on 2017/10/30.
 */
public class XmlHelper {

    public static <T extends Serializable> T fromWeixinXml(Class<T> clazz, String xmlRaw) throws Exception {
        String name = clazz.getSimpleName();
        String place1 = "<" + name + ">";
        String place2 = "</" + name + ">";

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + xmlRaw;
        xml = xml.replace("<xml>", place1).replace("</xml>", place2);
        return (T) org.jframe.core.helpers.XmlHelper.deserialize(clazz, xml);
    }
}
