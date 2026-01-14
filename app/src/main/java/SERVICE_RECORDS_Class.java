import java.util.Date;

public class SERVICE_RECORDS_Class {
    private Integer SERVICE_ID;
    private String ASSET_ID;
    private String USER_ID;
    private Date SERVICE_DATE;
    private String SERVICE_TYPE;
    private String PROBLEM_DESCRIPTION;
    private String SERVICE_NOTES;
    private String NEXT_ACTION_REQUIRED;

    public String getASSET_ID() {
        return ASSET_ID;
    }

    public void setASSET_ID(String ASSET_ID) {
        this.ASSET_ID = ASSET_ID;
    }

    public String getNEXT_ACTION_REQUIRED() {
        return NEXT_ACTION_REQUIRED;
    }

    public void setNEXT_ACTION_REQUIRED(String NEXT_ACTION_REQUIRED) {
        this.NEXT_ACTION_REQUIRED = NEXT_ACTION_REQUIRED;
    }

    public String getPROBLEM_DESCRIPTION() {
        return PROBLEM_DESCRIPTION;
    }

    public void setPROBLEM_DESCRIPTION(String PROBLEM_DESCRIPTION) {
        this.PROBLEM_DESCRIPTION = PROBLEM_DESCRIPTION;
    }

    public Date getSERVICE_DATE() {
        return SERVICE_DATE;
    }

    public void setSERVICE_DATE(Date SERVICE_DATE) {
        this.SERVICE_DATE = SERVICE_DATE;
    }

    public Integer getSERVICE_ID() {
        return SERVICE_ID;
    }

    public void setSERVICE_ID(Integer SERVICE_ID) {
        this.SERVICE_ID = SERVICE_ID;
    }

    public String getSERVICE_NOTES() {
        return SERVICE_NOTES;
    }

    public void setSERVICE_NOTES(String SERVICE_NOTES) {
        this.SERVICE_NOTES = SERVICE_NOTES;
    }

    public String getSERVICE_TYPE() {
        return SERVICE_TYPE;
    }

    public void setSERVICE_TYPE(String SERVICE_TYPE) {
        this.SERVICE_TYPE = SERVICE_TYPE;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
