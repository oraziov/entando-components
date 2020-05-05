package org.entando.entando.plugins.jacms.web.resource.request;

import java.util.List;

public class UpdateResourceRequest {
    private String description;
    private List<String> categories;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
