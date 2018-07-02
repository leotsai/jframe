package org.jframe.services.pc.impls;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.PageRequest;
import org.jframe.core.extensions.PageResult;
import org.jframe.services.core.ServiceBase;
import org.jframe.services.pc.CaseService;
import org.jframe.services.pc.dtos.CaseDetailDto;
import org.jframe.services.pc.dtos.CaseDto;
import org.springframework.stereotype.Service;

/**
 * Created by Leo on 2018/7/2.
 */
@Service("pc-case-service")
public class CaseServiceImpl extends ServiceBase implements CaseService {
    @Override
    public PageResult<CaseDto> Search(PageRequest request) {
        PageResult<CaseDto> result = new PageResult<>(request, new JList<>());

        if (request.getPageIndex() < 100) {
            for (int i = 0; i < request.getPageSize(); i++) {
                int id = request.getPageSize() * request.getPageIndex() + i + 1;
                CaseDto dto = new CaseDto();
                dto.setId((long) id);
                dto.setTitle("case 00" + id);
                dto.setLeadin("case 00" + id + " lead in......");
                result.getList().add(dto);
            }
        }
        result.setTotalPages(100);
        result.setTotalPages(request.getPageSize() * result.getTotalPages());
        return result;
    }

    @Override
    public CaseDetailDto get(Long id) {
        CaseDetailDto dto = new CaseDetailDto();
        dto.setId(id);
        dto.setTitle("case demo");
        dto.setKeywords("case demo keywords for id = " + id);
        dto.setDescription("this is case " + id + " description");
        dto.setContent("<h1>case " + id + "</h1> <p>hello case demo!</p>");
        return dto;
    }
}
