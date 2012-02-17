package com.TMAI.android.client.data;

public class EntityValidationResult {

	public enum EntityValidationResultType{
		ENTITY_EXISTS,
		ENTITY_DOES_NOT_EXISTS,
		CONNECTION_PROBLEM
	}
	private EntityValidationResultType entityValidationResultType;
	private String entityID;
	private String entityName;
	
	public EntityValidationResult() {
		super();
	}

	public EntityValidationResult(
			EntityValidationResultType entityValidationResultType,
			String entityID, String entityName) {
		super();
		this.entityValidationResultType = entityValidationResultType;
		this.entityID = entityID;
		this.entityName = entityName;
	}
	
	public EntityValidationResultType getEntityValidationResultType() {
		return entityValidationResultType;
	}
	public void setEntityValidationResultType(
			EntityValidationResultType entityValidationResultType) {
		this.entityValidationResultType = entityValidationResultType;
	}
	public String getEntityID() {
		return entityID;
	}
	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
