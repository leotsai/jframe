package org.jframe.web.controllers;

import org.apache.commons.io.FileUtils;
import org.jframe.core.helpers.StringHelper;
import org.jframe.infrastructure.AppContext;
import org.jframe.web.viewModels.StaticViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by leo on 2018-03-31.
 */
@Controller
public class StaticHtmlController extends _ControllerBase {

    @GetMapping(value = {"/", "/pc/**"})
    public ModelAndView partyEmployee(HttpServletRequest request) throws Exception {
        return super.tryView("/modules/pc/views/_shared/layout.jsp", () -> {
            String htmlPath = "/modules/pc/views/home/index.html";
            String path = request.getRequestURI();
            if (path.length() > 5) {
                path = path.substring(5);
                if (!StringHelper.isNullOrWhitespace(path)) {
                    htmlPath = "/modules/pc/views/" + path + ".html";
                }
            }
            StaticViewModel model = new StaticViewModel();
            String html = this.getHtml(htmlPath);
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

    //
    private String getHtml(String relativePath) throws Exception {
        Path physicalPath = Paths.get(AppContext.getStartupDirectory(), relativePath);
        return FileUtils.readFileToString(physicalPath.toFile(), "UTF-8");
    }
//
//    private void render(HttpServletResponse response, String htmlPath) throws IOException {
//        Path physicalPath = Paths.get(AppContext.getStartupDirectory(), htmlPath);
//        String html = FileUtils.readFileToString(physicalPath.toFile(), "UTF-8");
//        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().write(html);
//    }

    private void renderToResponse(HttpServletResponse response, String html) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(html);
    }
}
