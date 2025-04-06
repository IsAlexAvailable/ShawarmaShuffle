public enum MovementState {
    IDLE, LEFT, RIGHT, UP, DOWN, 
    NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST,
    HORIZONTAL, VERTICAL;

    public static final MovementState[] directions = {LEFT, RIGHT, UP, DOWN, IDLE, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST};
}