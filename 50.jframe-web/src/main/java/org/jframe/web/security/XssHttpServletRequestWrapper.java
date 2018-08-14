package org.jframe.web.security;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author qq
 * @date 2018/7/17
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static Pattern patterns = Pattern.compile("<script>(.*?)</script>"
            + "|src[\r\n]*=[\r\n]*\\\'(.*?)\\\'"
            + "|src[\r\n]*=[\r\n]*\\\"(.*?)\\\"" + "|</script>"
            + "|<script(.*?)>" + "|eval\\((.*?)\\)"
            + "|expression\\((.*?)\\)" + "|javascript:" + "|vbscript:"
            + "|onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            | Pattern.DOTALL);

    private static Pattern patterns2 = Pattern.compile("(<.*>.*</.*>)|(<.*/?>)", Pattern.CASE_INSENSITIVE
            | Pattern.MULTILINE
            | Pattern.DOTALL);

    private static Pattern comment = Pattern.compile("/\\*.*\\*/", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            | Pattern.DOTALL);

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return stripXSS(super.getHeader(name));
    }

    @Override
    public String getParameter(String parameter) {
        return stripXSS(super.getParameter(parameter));
    }

    @Override
    public String[] getParameterValues(String parameter) {
        return stripXSS(super.getParameterValues(parameter));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        if (parameterMap == null) {
            return null;
        }
        Map<String, String[]> newParameterMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            newParameterMap.put(entry.getKey(), stripXSS(entry.getValue()));
        }
        return Collections.unmodifiableMap(newParameterMap);
    }

    private static String stripXSS(String value) {
        if (value != null) {
//            value = value.replaceAll("\0", "");
//            value = patterns.matcher(value).replaceAll("");
//            value = patterns2.matcher(value).replaceAll("");
//            value = comment.matcher(value).replaceAll("");

            value = StringEscapeUtils.escapeHtml(value);
//            value = StringEscapeUtils.escapeJavaScript(value);
//            value = StringEscapeUtils.escapeSql(value);
        }
        return value;
    }

    private static String[] stripXSS(String[] values) {
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }


}
