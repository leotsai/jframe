package org.jframe.web.pc.viewModels;

import org.jframe.core.extensions.PageResult;
import org.jframe.services.pc.dtos.CaseDto;
import org.jframe.web.viewModels.LayoutViewModel;

/**
 * Created by Leo on 2018/7/2.
 */
public class CaseIndexViewModel extends LayoutViewModel {


    public CaseIndexViewModel(){
        this.setTitle("cases");
        this.setKeywords("cases, jframe");
        this.setDescription("cases");
    }

    private PageResult<CaseDto> cases;

    public PageResult<CaseDto> getCases() {
        return cases;
    }

    public void setCases(PageResult<CaseDto> cases) {
        this.cases = cases;
    }
}
