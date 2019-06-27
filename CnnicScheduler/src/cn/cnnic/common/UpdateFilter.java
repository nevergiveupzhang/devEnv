package cn.cnnic.common;

public class UpdateFilter
{
  private String setFields;
  private String whereString;
  
  public UpdateFilter() {}
  
  public UpdateFilter(String setFields, String whereString)
  {
    this.setFields = setFields;
    this.whereString = whereString;
  }
  
  public String getSetFields()
  {
    return this.setFields;
  }
  
  public void setSetFields(String setFields)
  {
    this.setFields = setFields;
  }
  
  public String getWhereString()
  {
    return this.whereString;
  }
  
  public void setWhereString(String whereString)
  {
    this.whereString = whereString;
  }
}
