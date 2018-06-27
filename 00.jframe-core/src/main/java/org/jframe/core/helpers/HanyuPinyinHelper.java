package org.jframe.core.helpers;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author luoh
 * @date 2018/4/19 17:05
 */
public class HanyuPinyinHelper {

    public static HanyuPinyinOutputFormat format = null;

    public enum Type {
        UPPERCASE,              //全部大写
        LOWERCASE,              //全部小写
        FIRSTUPPER              //首字母大写
    }

    static {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * 如： 明天 转换成 MINGTIAN
     *
     * @param str
     * @param spera
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toPinYin(String str, String spera, Type type, boolean onlyFirstLetter) throws BadHanyuPinyinOutputFormatCombination {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        if (type == Type.UPPERCASE) {
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        } else {
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }

        String pinyin = "";
        String pinyinTemp = "";
        String[] pinyinArray;
        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);
            if ((int) character <= 128) {
                pinyin += character;
            } else {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(character, format);
                if (pinyinArray == null) {
                    pinyin += character;
                } else {
                    pinyinTemp = pinyinArray[0];
                    if (type == Type.FIRSTUPPER) {
                        pinyinTemp = pinyinArray[0].toUpperCase().charAt(0) + pinyinTemp.substring(1);
                    }
                    if (onlyFirstLetter) {
                        pinyinTemp = String.valueOf(pinyinArray[0].charAt(0));
                    }
                    pinyin += pinyinTemp + (i == str.length() - 1 ? "" : spera);
                }
            }
        }
        return pinyin.trim();
    }
}
