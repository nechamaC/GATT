public class FinanceDetails {
    public FinanceDetails(int recordName) {
        this.recordName = recordName;
    }
    public FinanceDetails() {

    }
    public int getRecordName() {
        return recordName;
    }

    public void setRecordName(int recordName) {
        this.recordName = recordName;
    }

    int recordName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    String columnName;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String value;

    public void set(int record, String column,String val){
        setColumnName(column);
        setValue(val);
        setRecordName(record);
    }
}
