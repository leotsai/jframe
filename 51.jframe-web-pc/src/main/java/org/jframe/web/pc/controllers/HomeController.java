package org.jframe.web.pc.controllers;

import org.jframe.core.helpers.StringHelper;
import org.jframe.web.viewModels.LayoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Leo on 2018/7/2.
 */
@Controller
@RequestMapping
public class HomeController extends _PcControllerBase {

    @GetMapping(value = {"/", "/pc/**"})
    public ModelAndView partyEmployee(HttpServletRequest request) throws Exception {
        return super.tryView("_shared/layout.jsp", () -> {
            String htmlPath = super.getAreaPathPrefix() + "home/index.html";
            String path = request.getRequestURI();
            if (path.length() > 4) {
                path = path.substring(4);
                if (!StringHelper.isNullOrWhitespace(path)) {
                    htmlPath = super.getAreaPathPrefix() + path + ".html";
                }
            }
            LayoutViewModel model = new LayoutViewModel();
            String html = super.readFileText(htmlPath);
            int index = html.indexOf("</seo>");
            if (index > -1) {
                String seo = html.substring(0, index);
                html = html.substring(index + 6);
                model.setTitle(this.getTagHtml(seo, "title"));
                model.setKeywords(this.getTagHtml(seo, "keywords"));
                model.setDescription(this.getTagHtml(seo, "description"));
            }
            model.setHtml(html);
            return model;
        });
    }

    private String getTagHtml(String html, String tagName) {
        int index = html.indexOf("<" + tagName + ">");
        if (index > -1) {
            html = html.substring(index + tagName.length() + 2);
            index = html.indexOf("</" + tagName + ">");
            if (index > -1) {
                return html.substring(0, index);
            }
        }
        return null;
    }
}
