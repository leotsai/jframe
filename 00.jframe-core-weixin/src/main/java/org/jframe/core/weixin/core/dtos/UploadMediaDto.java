package org.jframe.core.weixin.core.dtos;

import org.jframe.core.weixin.core.enums.MediaType;

/**
 * Created by leo on 2017-09-24.
 */
public class UploadMediaDto extends _ApiDtoBase{
    private MediaType type ;
    private String media_id ;
    private long created_at ;

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
