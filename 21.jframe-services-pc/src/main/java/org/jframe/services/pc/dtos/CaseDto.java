package org.jframe.services.pc.dtos;

/**
 * Created by Leo on 2018/7/2.
 */
public class CaseDto {
    private Long id;
    private String title;
    private String leadin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeadin() {
        return leadin;
    }

    public void setLeadin(String leadin) {
        this.leadin = leadin;
    }
}
