package org.jframe.web.pc.controllers;

import org.jframe.core.extensions.PageRequest;
import org.jframe.infrastructure.AppContext;
import org.jframe.services.pc.CaseService;
import org.jframe.web.pc.viewModels.CaseDetailViewModel;
import org.jframe.web.pc.viewModels.CaseIndexViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Leo on 2018/7/2.
 */
@Controller
@RequestMapping("/pc/case")
public class CaseController extends _PcControllerBase {

    @GetMapping("")
    public ModelAndView index(@RequestParam(name = "i", required = false) Integer pageIndex){
        return super.tryView("pc-case-index", ()->{
            CaseIndexViewModel model = new CaseIndexViewModel();
            PageRequest request = new PageRequest(0, 10);
            if(pageIndex != null){
                request.setPageIndex(pageIndex);
            }
            model.setCases(AppContext.getBean(CaseService.class).Search(request));
            return model;
        });
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Long id){
        return super.tryView("pc-case-detail", ()-> new CaseDetailViewModel().build(id));
    }


}
