<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement" />&#32;/&#32;
        </a>
        <s:text name="title.messageManagement.trash" />  
    </span>
</h1>
<div id="main">
    <s:form action="delete" >
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <p class="noscreen"><wpsf:hidden name="id"/></p>
        <s:set var="id" value="message.id" />
        <s:set var="typeDescr" value="message.typeDescr" />
        <div class="alert alert-warning">
            <s:text name="title.messageManagement.trash.info" />:&#32;<code><s:text name="%{#id}"/></code>&#32;
            <s:text name="title.messageManagement.trash.info.type" />&#32;<em><s:property value="#typeDescr"/></em>?
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;    
                    <s:text name="%{getText('label.confirm')}"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
