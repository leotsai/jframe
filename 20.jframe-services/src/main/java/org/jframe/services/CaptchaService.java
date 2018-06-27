package org.jframe.services;

import org.jframe.data.enums.CaptchaUsage;

import java.util.List;

/**
 * Created by leo on 2017/5/16.
 */
public interface CaptchaService {
    void add(String phone, String code, CaptchaUsage usage);

    void validateSmsCaptcha(String phone, String code, CaptchaUsage usage);

    boolean isValid(String phone, String code, CaptchaUsage usage);

    void markLatestCaptchaUnused(String phone, CaptchaUsage usage, String code);

}
