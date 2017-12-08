package logic;

import java.io.Serializable;

public enum CollisionType implements Serializable{
	INTERSECTS, TOUCHES_LEFT, TOUCHES_RIGHT, TOUCHES_TOP, TOUCHES_BOTTOM, TOUCHES_TOP_LEFT, NO_COLLISION;
}
