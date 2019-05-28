package h2o.common.bean.result;


import org.apache.commons.lang.builder.ToStringBuilder;

public class ResultBool implements ErrorInfo , java.io.Serializable {

    private static final long serialVersionUID = 2317776198697479575L;

    public final boolean success;

    public final String msg;

    public ResultBool( boolean success , String msg ) {
        this.success = success;
        this.msg = msg;
    }


    public static ResultBool success() {
        return new ResultBool(true,null);
    }

    public static ResultBool success( String msg ) {
        return new ResultBool(true,msg);
    }

    public static ResultBool error( String msg ) {
        return new ResultBool(false,msg);
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }


    @Override
    public String errorCode() {
        return Boolean.toString(success);
    }

    @Override
    public String errorMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
