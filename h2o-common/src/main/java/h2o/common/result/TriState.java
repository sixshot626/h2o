package h2o.common.result;

public enum TriState {

    Success(0),Failure(9),Unknown(1);


    public final int val;

    TriState(int val) {
        this.val = val;
    }

}
