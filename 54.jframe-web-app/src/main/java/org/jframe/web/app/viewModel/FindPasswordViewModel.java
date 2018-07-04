package org.jframe.web.app.viewModel;

import org.jframe.core.helpers.StringHelper;
import org.jframe.web.viewModels.LayoutViewModel;

import java.util.regex.Pattern;

/**
 * @author:qq
 * @date:2017/10/30
 */
public class FindPasswordViewModel extends LayoutViewModel {
    private String origin;

    public FindPasswordViewModel build(String returnUrl) {
        super.setTitle("忘记密码");
        super.setValue(returnUrl);

        this.origin = "customer";
        if (StringHelper.isNullOrWhitespace(returnUrl)) {
            return this;
        }
        if (Pattern.matches("^/dealer.*$", returnUrl.toLowerCase())) {
            this.origin = "dealer";
            return this;
        }
        if (Pattern.matches("^/pe.*$", returnUrl.toLowerCase())) {
            this.origin = "employee";
        }
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

}
