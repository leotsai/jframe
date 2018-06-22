package org.jframe.web.viewModels;

/**
 * Created by Leo on 2017/9/25.
 */
public class MessageViewModel extends LayoutViewModel{
    private String icon = "error";
    private String buttonUrl="javascript:history.go(-1);";
    private String buttonText = "返回";

    public MessageViewModel(){

    }

    public MessageViewModel(String title){
        super.setTitle(title);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
