package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.InvalidFieldException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.ws.rs.BadRequestException;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.JsonUtils.getMapFromJson;

public class IoTUtils {

  public static FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public static String getMeasurementKey(String applicationToken, String loggerId, int version){
		return StringUtils.join(applicationToken,"_",loggerId,"_version",version);
	}

	public static HttpHeaders getHeaders(DashboardConfigDto server) {
		HttpHeaders headers = getAuthenticationHeader(server);
		headers.add("Content-Type", "application/json");
		return headers;
	}

	public static HttpHeaders getAuthenticationHeader(DashboardConfigDto dashboardConfigDto) {
		String encoding = Base64.getEncoder().encodeToString(
				StringUtils.join(dashboardConfigDto.getUsername(), ":", dashboardConfigDto.getPassword()).getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, StringUtils.join("Basic ", encoding));
		return headers;
	}

	public static <T> String formatQueryUrl(Map<String, T> params) {
		String query = "";
		for (Entry<String, T> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				query = StringUtils.join(query, entry.getKey(), "=", entry.getValue(), "&");
			}
		}
		if(query.length() > 1) {
			return query.substring(0, query.length() - 1);
		}
		return query;
	}

  public static ResponseEntity<String> getRequestMethod(String url, int requestTimeout, HttpHeaders headers) {
    HttpEntity httpEntity = new HttpEntity(headers);

    RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory(requestTimeout));
    try {
      return restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    }
    catch (Throwable t){
      return new ResponseEntity(t, HttpStatus.REQUEST_TIMEOUT);
    }
  }

  private static ClientHttpRequestFactory getClientHttpRequestFactory(int requestTimeout) {
    int timeout = requestTimeout;
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
        = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(timeout);
    return clientHttpRequestFactory;
  }
  
  public static ResponseEntity<String> postRequestMethod(String url, int requestTimeout, HttpHeaders headers, JsonObject body) {
    HttpEntity<?> httpEntity = new HttpEntity<Object>(getMapFromJson(body), headers);
    
    RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory(requestTimeout));
    try {
      return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }
    catch (ResourceAccessException | IllegalArgumentException e){
      return new ResponseEntity(e, HttpStatus.REQUEST_TIMEOUT);
    }
  }
  
  public static ResponseEntity<String> postRequestMethod(String url, int requestTimeout, HttpHeaders headers, String body) {
    HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
    
    RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory(requestTimeout));
    try {
      return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }
    catch (ResourceAccessException | IllegalArgumentException e){
      return new ResponseEntity(e, HttpStatus.REQUEST_TIMEOUT);
    }
  }

	public static boolean isValidResponse(ResponseEntity<String> response) {
    return response.getStatusCode().is2xxSuccessful();
  }

  public static DatasourcesConfigDto getDatasource(DashboardConfigDto dto,
      String datasourceCode) {
    Optional<DatasourcesConfigDto> optDatasource = dto.getDatasources().stream()
        .filter(d -> d.getDatasourceCode().equals(datasourceCode)).findFirst();
    return optDatasource.orElseThrow(() -> new InvalidFieldException(
        String.format("no datasource with code %s", datasourceCode)));
  }
  
  public static void throwApiResourceUnavailableEx(ResponseEntity<String> response,
      int dashboardId, String datasourceCode, Class aClass) {
	  if(datasourceCode != null) {
      final Logger logger = LoggerFactory.getLogger(aClass);
      logger.error(IoTConstants.LOG_API_EX_DASHBOARD_DATASOURCE,
          Thread.currentThread().getStackTrace()[2].getMethodName(),
          response.getStatusCode(),
          dashboardId, datasourceCode);
      throw new ApiResourceNotAvailableException(
          String.valueOf(response.getStatusCodeValue()),
          String.format(
              IoTConstants.API_EX_DASHBOARD_DATASOURCE, aClass.getSimpleName(),
              dashboardId, datasourceCode));
    } else {
      final Logger logger = LoggerFactory.getLogger(aClass);
      logger.error(IoTConstants.LOG_API_EX_DASHBOARD,
          Thread.currentThread().getStackTrace()[2].getMethodName(),
          response.getStatusCode(),
          dashboardId);
      throw new ApiResourceNotAvailableException(
          String.valueOf(response.getStatusCodeValue()),
          String.format(
              IoTConstants.API_EX_DASHBOARD, aClass.getSimpleName(),
              dashboardId));
    }
  }

  public static void logEndMethod(String dashboard, String datasource, boolean result, Class aClass) {
    final Logger logger = LoggerFactory.getLogger(aClass);
    String message = "{} method {} dashboard: {} datasource: {} result {}";
    if(datasource == null) {
      message = "{} method {} dashboard: {} result {}";
    }
    logger.info(message, aClass.getClass().getSimpleName(),
        Thread.currentThread().getStackTrace()[3].getMethodName(),
        dashboard, datasource, result);
  }
  
  public static void logEndMethod(boolean result, Class aClass) {
    final Logger logger = LoggerFactory.getLogger(aClass);
    String message = "{} method {} result {}";
    logger.info(message, aClass.getSimpleName(),
        Thread.currentThread().getStackTrace()[3].getMethodName(), result);
  }

  public static void logEndMethod(int dashboardId, String datasource, boolean result, Class aClass) {
    logEndMethod(String.valueOf(dashboardId),datasource,result,aClass);
    
  }

  public static void logStartMethod(String dashboard, String datasource, Class aClass) {
    final Logger logger = LoggerFactory.getLogger(aClass);
    String message = "{} method {} dashboard: {} datasource: {}";
    if(datasource == null) {
      message = "{} method {} dashboard: {}";
    }
    logger.info(message, aClass.getSimpleName(),
        Thread.currentThread().getStackTrace()[3].getMethodName(),
        dashboard, datasource);
  }
  
  public static void logStartMethod(Class aClass) {
    final Logger logger = LoggerFactory.getLogger(aClass);
    String message = "{} method {}";
    logger.info(message, aClass.getSimpleName(),
        Thread.currentThread().getStackTrace()[3].getMethodName());
  }

  public static void logStartMethod(int dashboardId, String datasource, Class aClass) {
    logStartMethod(String.valueOf(dashboardId),datasource,aClass);
  }

    public static DashboardConfigDto checkServerAndDatasource(int serverId, String datasourceCode, IDashboardConfigService dashboardConfigService) {
    if (!dashboardConfigService.existsByIdAndIsActive(serverId)) {
      throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Server", String.valueOf(serverId));
    }
    DashboardConfigDto dashboardConfig = dashboardConfigService.getDashboardConfig(serverId);
    if (dashboardConfig.getDatasources().stream().filter(d -> d.getDatasourceCode().equals(datasourceCode)).count() <= 0) {
      throw new ResourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Datasource" , datasourceCode);
    }
    return dashboardConfig;
  }

  public static MeasurementConfig getMeasurementConfig(DashboardConfigDto dto, String datasourceCode,
      MeasurementTemplate template) {
    MeasurementConfig measurementConfig = new MeasurementConfig();
    measurementConfig.setDashboardId(dto.getId());
    measurementConfig.setDatasourceCode(datasourceCode);
    measurementConfig.setMeasurementTemplateId(template.getId());
    for (MeasurementType measurementType : template.getFields()) {
      MeasurementMapping mapping = new MeasurementMapping();
      mapping.setSourceName(measurementType.getName());
      mapping.setDestinationName(measurementType.getName());
      measurementConfig.getMappings().add(mapping);
    }
    return measurementConfig;
  }
}