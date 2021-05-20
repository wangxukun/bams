package org.xkidea.bams.web;

import org.xkidea.bams.ejb.CustomerBean;
import org.xkidea.bams.entity.Groups;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.qualifiers.LoggedIn;
import org.xkidea.bams.web.util.JsfUtil;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable {

    private static final String BUNDLE = "bundles.Bundle";
    private static final long serialVersionUID = -3197592665392201393L;

    private static final Logger logger =
            Logger.getLogger("bams.web.UserController");

    Person user;
    @EJB
    private CustomerBean ejbFacade;
    private String username;
    private String password;
    @Inject
    CustomerController customerController;

    public UserController() {
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        String result ;

        try {
            request.login(this.getUsername(), this.getPassword());

//            JsfUtil.addSuccessMessage(JsfUtil.getStringFromBundle(BUNDLE, "Login_Success"));

            this.user = ejbFacade.getUserByEmail(getUsername());
            this.getAuthenticatedUser();

            if (isAdmin()) {
                result = "/admin/index?faces-redirect=true";
            } else if(isTreasurer()){
                result = "/bookkeeping/index?faces-redirect=true";
            } else{
                result = "/query/index?faces-redirect=true";
            }
        } catch (ServletException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE,null,ex);
            JsfUtil.addErrorMessage(JsfUtil.getStringFromBundle(BUNDLE,"Login_Failed"));

            result = "login";
        }

        return result;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            this.user = null;
            request.logout();
            // clear the session;
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
            JsfUtil.addSuccessMessage(JsfUtil.getStringFromBundle(BUNDLE, "Logout_Success"));
        } catch (ServletException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE,null,ex);
            JsfUtil.addErrorMessage(JsfUtil.getStringFromBundle(BUNDLE,"Logout_Failed"));
        }finally {
            return "/login";
        }
    }

    public
    @Produces
    @LoggedIn
    Person getAuthenticatedUser() {
        return user;
    }

    public boolean isLogged() {
        return (getUser() == null) ? false : true;
    }

    public boolean isAdmin(){
        for (Groups g : user.getGroupsList()){
            if (g.getName().equals("ADMINISTRATOR")) {
                return true;
            }
        }
        return false;
    }

    public boolean isTreasurer() {
        for (Groups g : user.getGroupsList()) {
            if (g.getName().equals("TREASURER")) {
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getUser() {
        return user;
    }

    public CustomerBean getEjbFacade() {
        return ejbFacade;
    }
}
