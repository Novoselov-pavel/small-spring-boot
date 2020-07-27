package com.npn.spring.learning.spring.smallspringboot.model.html;

public class HtmlThymeleafPage {
    private static final String thymeleafObjectName = "htmlThymeleafPage";
    private String title;
    private String fragmentName;
    private boolean hideMessage = true;
    private String message;
    private String navName;

    public static String getThymeleafObjectName() {
        return thymeleafObjectName;
    }

    public boolean isHideMessage() {
        return hideMessage;
    }

    public void setHideMessage(boolean hideMessage) {
        this.hideMessage = hideMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }
}
