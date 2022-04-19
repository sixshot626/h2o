package h2o.common.data.domain;

public class SortInfo implements java.io.Serializable {

    private static final long serialVersionUID = 4094699421665186848L;

    public enum Direction {
        ASC, DESC
    }


    private final String name;

    private final Direction direction;

    public SortInfo(String name) {
        this.name = name;
        this.direction = Direction.ASC;
    }

    public SortInfo(String name, Direction direction) {
        this.name = name;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public String toSqlString() {
        if (direction == null || direction == Direction.ASC) {
            return name;
        } else {
            return name + " desc";
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SortInfo{");
        sb.append("name='").append(name).append('\'');
        sb.append(", direction=").append(direction);
        sb.append('}');
        return sb.toString();
    }
}
