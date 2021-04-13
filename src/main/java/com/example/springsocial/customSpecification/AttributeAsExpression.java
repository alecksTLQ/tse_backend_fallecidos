package com.example.springsocial.customSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@SuppressWarnings("rawtypes")
public class AttributeAsExpression<T>{
	private Root<?> root;
	private String attribute, option;
	private String[] attributeArray;
	private Class<? extends Object> type;
	private CriteriaBuilder criteriaBuilder;
	
	// SET	
	public void setRoot(Root root) {this.root=root;};
	public void setAttribute(String attribute) {this.attribute=attribute;};
	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {this.criteriaBuilder=criteriaBuilder;};
	public void setType(Class<? extends Object> type) {this.type=type;}
	public void setOption(String option) {this.option=option;}
	
	//GET
	public Class<? extends Object> getType(){return this.type;}
	public String getTypeNameLowerCase() { return this.type.getSimpleName().toString().toLowerCase(); }
	
	//SET LOCAL
	private void setAttributePars() { this.attributeArray = this.attribute.split("[.]"); }
	public void setType() {
		this.setAttributePars();
		if (this.attributeArray.length==1) this.type=root.get(this.attributeArray[0]).getJavaType();
		if (this.attributeArray.length==2) this.type=root.get(this.attributeArray[0]).get(this.attributeArray[1]).getJavaType();
		if (this.attributeArray.length==3) this.type=root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).getJavaType();
		if (this.attributeArray.length==4) this.type=root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).get(this.attributeArray[3]).getJavaType();
	}
	
	//GET
	public Expression<?> get(){
		if ( (this.option.equalsIgnoreCase("Contiene") || this.option.equalsIgnoreCase("Inicia") || this.option.equalsIgnoreCase("Finaliza")) && this.type.getSimpleName().toString().equalsIgnoreCase("date")) {
			if  (this.attributeArray.length==1)
				return (Expression<String>) this.criteriaBuilder.lower(this.criteriaBuilder.function("dbo.convertDateToStringFrenchFormat", String.class, root.get(this.attributeArray[0])));
			else if  (this.attributeArray.length==2) 
				return (Expression<String>) this.criteriaBuilder.lower(this.criteriaBuilder.function("dbo.convertDateToStringFrenchFormat", String.class, root.get(this.attributeArray[0]).get(this.attributeArray[1])  ));
			else if  (this.attributeArray.length==3) 
				return (Expression<String>) this.criteriaBuilder.lower(this.criteriaBuilder.function("dbo.convertDateToStringFrenchFormat", String.class, root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]) ));
			else if  (this.attributeArray.length==4) 
				return (Expression<String>) this.criteriaBuilder.lower(this.criteriaBuilder.function("dbo.convertDateToStringFrenchFormat", String.class, root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).get(this.attributeArray[3]) ));
		}
		
		if ( this.option.equalsIgnoreCase("Contiene") || this.option.equalsIgnoreCase("Inicia") || this.option.equalsIgnoreCase("Finaliza") ) {
			if  (this.attributeArray.length==1) return (Expression<String>) this.criteriaBuilder.lower(root.get(this.attributeArray[0]).as(String.class));
			else if  (this.attributeArray.length==2) return (Expression<String>) this.criteriaBuilder.lower(root.get(this.attributeArray[0]).get(this.attributeArray[1]).as(String.class));
			else if  (this.attributeArray.length==3) return (Expression<String>) this.criteriaBuilder.lower(root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).as(String.class));
			else if  (this.attributeArray.length==4) return (Expression<String>) this.criteriaBuilder.lower(root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).get(this.attributeArray[3]).as(String.class));
			else return root;
		}else {
			if  (this.attributeArray.length==1)  
				return root.get(this.attributeArray[0]).as(this.type);
			else if  (this.attributeArray.length==2) 
				return root.get(this.attributeArray[0]).get(this.attributeArray[1]).as(this.type);
			else if  (this.attributeArray.length==3) 
				return root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).as(this.type);
			else if  (this.attributeArray.length==4) 
				return root.get(this.attributeArray[0]).get(this.attributeArray[1]).get(this.attributeArray[2]).get(this.attributeArray[3]).as(this.type);
			else return root;
		}
	}
	 
}
