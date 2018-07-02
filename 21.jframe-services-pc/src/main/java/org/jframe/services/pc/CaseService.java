package org.jframe.services.pc;

import org.jframe.core.extensions.PageRequest;
import org.jframe.core.extensions.PageResult;
import org.jframe.services.pc.dtos.CaseDetailDto;
import org.jframe.services.pc.dtos.CaseDto;

/**
 * Created by Leo on 2018/7/2.
 */
public interface CaseService {
    PageResult<CaseDto> Search(PageRequest request);
    CaseDetailDto get(Long id);
}
