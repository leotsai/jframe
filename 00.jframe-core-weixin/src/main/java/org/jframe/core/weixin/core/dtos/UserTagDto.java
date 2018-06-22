package org.jframe.core.weixin.core.dtos;

import java.util.List;

/**
 * Created by leo on 2017-09-24.
 */
public class UserTagDto  extends _ApiDtoBase{

    private List<TagItem> tags;

    public List<TagItem> getTags() {
        return tags;
    }

    public void setTags(List<TagItem> tags) {
        this.tags = tags;
    }

    public class TagItem{
        private int id ;
        private String name ;
        private int count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
