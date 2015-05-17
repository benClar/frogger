public interface GameObject {

	Movement move(Direction d, Movement moveCalculator);
	GameObjectType getGameObjectType();
	Direction getDirection();
	int inheritSpeed();
	Direction inheritDirection();
	boolean inheritable();
	void setDirection(Direction d);
	void setInheritanceStatus(boolean i);
	void addToSpeed(int speed);
	void removeInheritanceSpeed();
	boolean ready();
	boolean justBeenMoved();
	double getMoveInterval();
	void makeReady();
}
