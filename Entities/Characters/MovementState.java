package Entities.Characters;

public enum MovementState {
    IDLE, LEFT, RIGHT, UP, DOWN, 
    NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST,
    HORIZONTAL, VERTICAL;

    public static final MovementState[] directions = {IDLE, LEFT, RIGHT, IDLE, UP, DOWN, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST};
}