public interface GameObject {

	Movement move(Direction d, Movement moveCalculator);
	GameObjectType getGameObjectType();
	Direction getDirection();
}