package org.xkidea.bams.web;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Named(value = "breadcrumb")
@SessionScoped
public class BreadcrumbController implements Serializable {

    private static final long serialVersionUID = 408861975740613420L;
    private Map<String,String> links;

    private HttpServletRequest request;

    public BreadcrumbController() {
        links = new LinkedHashMap<>();
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        String url = request.getRequestURI();
        links.put("主页",url);
        System.out.println("-----" + url);
        links.put("系统管理","/admin/index");
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
