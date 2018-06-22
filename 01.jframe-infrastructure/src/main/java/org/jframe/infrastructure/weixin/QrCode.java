package org.jframe.infrastructure.weixin;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;

/**
 * Created by Leo on 2017/10/30.
 */
public class QrCode {
    private String raw ;
    private boolean isPermanent;
    private QrCodePermanentType permanentType;
    private String permanentValue;
    private Long tempQrId;

    public static QrCode tryParseFromEventKey(String eventKey)
    {
        try
        {
            if(StringHelper.isNullOrWhitespace(eventKey)){
                return null;
            }
            QrCode code = new QrCode();
            code.raw = eventKey.replace("qrscene_", "").replace("|", ",");
            if(StringHelper.isNullOrWhitespace(code.raw)){
                return null;
            }
            if (code.raw.indexOf(",") < 0)
            {
                code.tempQrId = Long.valueOf(code.raw);
                code.isPermanent = false;
            }
            else
            {
                JList<String> parts = JList.splitByComma(code.raw);
                code.permanentType = QrCodePermanentType.from(Integer.valueOf(parts.get(0)));
                code.permanentValue = parts.get(1);
                code.isPermanent = true;
            }
            return code;
        }
        catch (Exception ex)
        {
            LogHelper.logRaw("weixin.eventkey.parse", eventKey);
            return null;
        }
    }

    public String getRaw() {
        return raw;
    }

    public boolean isPermanent() {
        return isPermanent;
    }


    public Long getTempQrId() {
        return tempQrId;
    }

    public QrCodePermanentType getPermanentType() {
        return permanentType;
    }

    public String getPermanentValue() {
        return permanentValue;
    }
}
