package org.jframe.core.unionpay.configs;

import org.jframe.core.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/11/8.
 */
public class ResponseCode {

    private static final Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put("00", "成功");
        map.put("A1", "有缺陷的成功");
        map.put("01", "交易失败。详情请咨询95516");
        map.put("02", "系统未开放或暂时关闭，请稍后再试");
        map.put("03", "交易通讯超时，请发起查询交易");
        map.put("04", "交易状态未明，请查询对账结果");
        map.put("05", "交易已受理，请稍后查询交易结果");
        map.put("06", "系统繁忙，请稍后再试");
        map.put("10", "报文格式错误");
        map.put("11", "验证签名失败");
        map.put("12", "重复交易");
        map.put("13", "报文交易要素缺失");
        map.put("14", "批量文件格式错误");
        //---------------------------------------------
        map.put("30", "交易未通过，请尝试使用其他银联卡支付或联系95516");
        map.put("31", "商户状态不正确");
        map.put("32", "无此交易权限");
        map.put("33", "交易金额超限");
        map.put("34", "查无此交易");
        map.put("35", "原交易不存在或状态不正确");
        map.put("36", "与原交易信息不符");
        map.put("37", "已超过最大查询次数或操作过于频繁");
        map.put("38", "银联风险受限");
        map.put("39", "交易不在受理时间范围内");
        map.put("40", "绑定关系检查失败");
        map.put("41", "批量状态不正确，无法下载");
        map.put("42", "扣款成功但交易超过规定支付时间");
        map.put("43", "无此业务权限，详情请咨询95516");
        map.put("44", "输入号码错误或暂未开通此项业务，详情请咨询95516");
        map.put("45", "原交易已被成功退货或已被成功撤销");
        //----------------------------------------------
        map.put("60", "交易失败，详情请咨询您的发卡行");
        map.put("61", "输入的卡号无效，请确认后输入");
        map.put("62", "交易失败，发卡银行不支持该商户，请更换其他银行卡");
        map.put("63", "卡状态不正确");
        map.put("64", "卡上的余额不足");
        map.put("65", "输入的密码、有效期或CVN2有误，交易失败");
        map.put("66", "持卡人身份信息或手机号输入不正确，验证失败");
        map.put("67", "密码输入次数超限");
        map.put("68", "您的银行卡暂不支持该业务，请向您的银行或95516咨询");
        map.put("69", "您的输入超时，交易失败");
        map.put("70", "交易已跳转，等待持卡人输入");
        map.put("71", "动态口令或短信验证码校验失败");
        map.put("72", "您尚未在{}银行网点柜面或个人网银签约加办银联无卡支付业务，请去柜面或网银开通或拨打{}");
        map.put("73", "支付卡已超过有效期");
        map.put("74", "扣款成功，销账未知");
        map.put("75", "扣款成功，销账失败");
        map.put("76", "需要验密开通");
        map.put("77", "银行卡未开通认证支付");
        map.put("78", "发卡行交易权限受限，详情请咨询您的发卡行");
        map.put("79", "此卡可用，但发卡行暂不支持短信验证");
        map.put("80", "交易失败，Token 已过期");
        map.put("81", "月累计交易笔数(金额)超限");
        map.put("82", "需要校验密码");
        map.put("83", "发卡行（渠道）处理中");
        map.put("84", "需要密码但未上送");
        //----------------------------------------------
        map.put("90", "账户余额不足");
        map.put("91", "认证失败");
        map.put("92", "营业执照过期");
        map.put("93", "营业执照吊销");
        map.put("94", "营业执照注销");
        map.put("95", "营业执照迁出");
        map.put("96", "营业执照撤销");
        map.put("98", "文件不存在");
        map.put("99", "通用错误");
    }

    public static String getMessage(String code) {
        if (StringHelper.isNullOrWhitespace(code)) {
            return null;
        }
        if (map.containsKey(code)) {
            return map.get(code);
        }
        return null;
    }

}
