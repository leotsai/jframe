package org.jframe.web.pc.viewModels;

import org.jframe.infrastructure.AppContext;

import org.jframe.services.pc.CaseService;
import org.jframe.services.pc.dtos.CaseDetailDto;
import org.jframe.web.viewModels.LayoutViewModel;

/**
 * Created by Leo on 2018/7/2.
 */
public class CaseDetailViewModel extends LayoutViewModel {

    private CaseDetailDto detail;

    public CaseDetailViewModel build(Long id){
        this.detail = AppContext.getBean(CaseService.class).get(id);
        this.setTitle(this.detail.getTitle());
        this.setKeywords(this.detail.getKeywords());
        this.setDescription(this.detail.getDescription());
        return this;
    }

    public CaseDetailDto getDetail() {
        return detail;
    }

    public void setDetail(CaseDetailDto detail) {
        this.detail = detail;
    }
}
