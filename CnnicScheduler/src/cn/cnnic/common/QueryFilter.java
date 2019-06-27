package cn.cnnic.common;

public class QueryFilter
{
  private String selectFields;
  private String whereString;
  private String orderString;
  
  public QueryFilter() {}
  
  public QueryFilter(String selectFields, String whereString, String orderString)
  {
    this.selectFields = selectFields;
    this.whereString = whereString;
    this.orderString = orderString;
  }
  
  public String getSelectFields()
  {
    return this.selectFields;
  }
  
  public void setSelectFields(String selectFields)
  {
    this.selectFields = selectFields;
  }
  
  public String getWhereString()
  {
    return this.whereString;
  }
  
  public void setWhereString(String whereString)
  {
    this.whereString = whereString;
  }
  
  public String getOrderString()
  {
    return this.orderString;
  }
  
  public void setOrderString(String orderString)
  {
    this.orderString = orderString;
  }

@Override
public String toString() {
	return "QueryFilter [selectFields=" + selectFields + ", whereString=" + whereString + ", orderString=" + orderString
			+ "]";
}
}
