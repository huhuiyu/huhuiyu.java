package cn.huhuiyu.database.codebuilder;

import cn.huhuiyu.beanutil.InfoBean;
import cn.huhuiyu.fileutil.FileUtil;

public class NameInfo extends InfoBean {
	private static final long serialVersionUID = 5415275400265103614L;
	private String entityPackage;
	private String daoPackage;
	private String daoImplPackage;
	private String servicePackage;
	private String serviceImplPackage;
	private String actionPackage;
	private String basicDaoPackage;

	private String entityName;
	private String daoName;
	private String daoImplName;
	private String serviceName;
	private String serviceImplName;
	private String actionName;
	private String basicDaoName;
	private String jspName;
	private String jspModifyName;
	private String jsonName;

	public NameInfo() {
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public String getEntityPackageDir() {
		return FileUtil.parsePackage(getEntityPackage());
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public String getDaoPackage() {
		return daoPackage;
	}

	public String getDaoPackageDir() {
		return FileUtil.parsePackage(getDaoPackage());
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public String getDaoImplPackage() {
		return daoImplPackage;
	}

	public String getDaoImplPackageDir() {
		return FileUtil.parsePackage(getDaoImplPackage());
	}

	public void setDaoImplPackage(String daoImplPackage) {
		this.daoImplPackage = daoImplPackage;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public String getServicePackageDir() {
		return FileUtil.parsePackage(getServicePackage());
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getServiceImplPackage() {
		return serviceImplPackage;
	}

	public String getServiceImplPackageDir() {
		return FileUtil.parsePackage(getServiceImplPackage());
	}

	public void setServiceImplPackage(String serviceImplPackage) {
		this.serviceImplPackage = serviceImplPackage;
	}

	public String getBasicDaoPackage() {
		return basicDaoPackage;
	}

	public String getBasicDaoPackageDir() {
		return FileUtil.parsePackage(getBasicDaoPackage());
	}

	public void setBasicDaoPackage(String basicDaoPackage) {
		this.basicDaoPackage = basicDaoPackage;
	}

	public String getActionPackage() {
		return actionPackage;
	}

	public String getActionPackageDir() {
		return FileUtil.parsePackage(getActionPackage());
	}

	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getDaoName() {
		return daoName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	public String getDaoImplName() {
		return daoImplName;
	}

	public void setDaoImplName(String daoImplName) {
		this.daoImplName = daoImplName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getBasicDaoName() {
		return basicDaoName;
	}

	public void setBasicDaoName(String basicDaoName) {
		this.basicDaoName = basicDaoName;
	}

	public String getJspName() {
		return jspName;
	}

	public void setJspName(String jspName) {
		this.jspName = jspName;
	}

	public String getJspModifyName() {
		return jspModifyName;
	}

	public void setJspModifyName(String jspModifyName) {
		this.jspModifyName = jspModifyName;
	}

	public String getJsonName() {
		return jsonName;
	}

	public void setJsonName(String jsonName) {
		this.jsonName = jsonName;
	}

}
