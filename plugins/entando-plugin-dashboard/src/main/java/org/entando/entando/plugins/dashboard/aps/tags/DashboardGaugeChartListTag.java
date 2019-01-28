/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.agiletec.aps.system.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.IDashboardGaugeChartManager;

public class DashboardGaugeChartListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardGaugeChartListTag.class);

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		IDashboardGaugeChartManager dashboardGaugeChartManager = (IDashboardGaugeChartManager) ApsWebApplicationUtils.getBean("dashboardDashboardGaugeChartManager", this.pageContext);
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			/*
			Widget widget = this.extractWidget(reqCtx);
			ApsProperties widgetConfig = widget.getConfig();
			*/
			List<Integer> list = dashboardGaugeChartManager.getDashboardGaugeCharts();
			this.pageContext.setAttribute(this.getVar(), list);
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			throw new JspException("Error in DashboardGaugeChartListTag doStartTag", t);
		}
		return super.doStartTag();
	}

/*
	private Widget extractWidget(RequestContext reqCtx) {
		Widget widget = null;
		widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		return widget;
	}

	protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
		return (String) widgetConfig.get(parameterName);
	}
*/
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		super.release();
		this.setVar(null);
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _var;

}
