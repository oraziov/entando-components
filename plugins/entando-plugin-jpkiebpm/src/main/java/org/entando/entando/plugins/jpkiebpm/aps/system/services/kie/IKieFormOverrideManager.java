package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.List;

public interface IKieFormOverrideManager {

    public String BEAN_ID = "jpkiebpmFormOverrideManager";

    public KieFormOverride getKieFormOverride(int id) throws ApsSystemException;

    public List<Integer> getKieFormOverrides() throws ApsSystemException;

    public List<Integer> searchKieFormOverrides(FieldSearchFilter filters[]) throws ApsSystemException;

    public void addKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException;

    public void updateKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException;

    public void deleteKieFormOverride(int id) throws ApsSystemException;

    public void deleteWidgetKieFormOverrides(int widgetInfoId) throws ApsSystemException;
    
    public void deleteWidgetKieFormOverrides(int widgetInfoId, boolean online) throws ApsSystemException;
    
    /**
     * Return all the overrides of the form generated by the given contaner and
     * process
     *
     * @param containerId
     * @param processId
     * @return
     * @throws ApsSystemException
     */
    @Deprecated
    public List<KieFormOverride> getFormOverrides(String containerId, String processId) throws ApsSystemException;
    
    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online, String containerId, String processId, String sourceId) throws ApsSystemException;

    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online, String containerId, String processId, String sourceId, String field) throws ApsSystemException;

    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online) throws ApsSystemException;
}
