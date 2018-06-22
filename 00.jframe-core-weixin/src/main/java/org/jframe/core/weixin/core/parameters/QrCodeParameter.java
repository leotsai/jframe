package org.jframe.core.weixin.core.parameters;

/**
 * Created by leo on 2017-09-24.
 */
public class QrCodeParameter {

    private String action_name;
    private int expire_seconds;
    private ActionInfo action_info = new ActionInfo();

    public QrCodeParameter(){

    }

    public QrCodeParameter(String action_name, String scene){
        this.action_name = action_name;
        this.action_info.scene.setScene_str(scene);
    }

    public QrCodeParameter(String action_name, int expireSeconds, Long sceneId){
        this.action_name = action_name;
        this.expire_seconds = expireSeconds;
        this.action_info.scene.setScene_id(sceneId);
    }

    public int getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(int expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    private class ActionInfo{
        private  Scene scene = new Scene();

        public Scene getScene() {
            return scene;
        }

        public void setScene(Scene scene) {
            this.scene = scene;
        }

        private class Scene{
            private String scene_str;
            private Long scene_id;

            public String getScene_str() {
                return scene_str;
            }

            public void setScene_str(String scene_str) {
                this.scene_str = scene_str;
            }

            public Long getScene_id() {
                return scene_id;
            }

            public void setScene_id(Long scene_id) {
                this.scene_id = scene_id;
            }
        }

    }


    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public void setAction_info(ActionInfo action_info) {
        this.action_info = action_info;
    }


}
