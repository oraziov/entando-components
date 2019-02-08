/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.digitalexchange.job;

import com.agiletec.aps.system.SystemConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.entando.entando.aps.system.init.model.servdb.DigitalExchangeComponentJobs;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = DigitalExchangeComponentJobs.TABLE_NAME)
public class DigitalExchangeJob {

    @Id
    private String id;

    @Column(name = DigitalExchangeComponentJobs.COL_DIGITAL_EXCHANGE_ID, length = 20)
    private String digitalExchangeId;

    @JsonIgnore
    @Column(name = DigitalExchangeComponentJobs.COL_DIGITAL_EXCHANGE_URL)
    private String digitalExchangeUrl;

    @Column(name = DigitalExchangeComponentJobs.COL_COMPONENT_ID)
    private String componentId;

    @Column(name = DigitalExchangeComponentJobs.COL_COMPONENT_NAME)
    private String componentName;

    @Column(name = DigitalExchangeComponentJobs.COL_COMPONENT_VERSION)
    private String componentVersion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    @Column(name = DigitalExchangeComponentJobs.COL_STARTED_AT)
    private Date started;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    @Column(name = DigitalExchangeComponentJobs.COL_ENDED_AT)
    private Date ended;

    @Column(name = DigitalExchangeComponentJobs.COL_STARTED_BY)
    private String user;

    @Column(name = DigitalExchangeComponentJobs.COL_PROGRESS)
    private double progress;

    @Enumerated(value=EnumType.STRING)
    @Column(name = DigitalExchangeComponentJobs.COL_STATUS )
    private JobStatus status;

    @Enumerated(value=EnumType.STRING)
    @Column(name = DigitalExchangeComponentJobs.COL_JOB_TYPE)
    private JobType jobType;

    @Column(name = DigitalExchangeComponentJobs.COL_ERROR_MESSAGE)
    private String errorMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDigitalExchangeId() {
        return digitalExchangeId;
    }

    public void setDigitalExchangeId(String digitalExchangeId) {
        this.digitalExchangeId = digitalExchangeId;
    }

    public String getDigitalExchangeUrl() {
        return digitalExchangeUrl;
    }

    public void setDigitalExchangeUrl(String digitalExchangeUrl) {
        this.digitalExchangeUrl = digitalExchangeUrl;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

}
