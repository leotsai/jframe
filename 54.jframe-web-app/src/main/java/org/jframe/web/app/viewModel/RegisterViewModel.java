package org.jframe.web.app.viewModel;

import org.jframe.core.helpers.StringHelper;
import org.jframe.web.viewModels.LayoutViewModel;

/**
 * created by yezi on 2017/11/1
 */
public class RegisterViewModel extends LayoutViewModel {

    private String phone;

    public RegisterViewModel buildPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RegisterViewModel() {

    }

    public RegisterViewModel(String title) {
        super(title);
    }

    public RegisterViewModel buildReturnUrl(String returnUrl) {
        if (StringHelper.isNullOrWhitespace(returnUrl)) {
            returnUrl = "/app";
        }
        super.setValue(returnUrl);
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
