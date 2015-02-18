package pt314.blocks.game;

/**
 * This is that target block, which must be moved out of the board.
 */
public class TargetBlock extends Block {

	public TargetBlock() {}
	
	public final boolean target = true;

	@Override
	public boolean isValidDirection(Direction dir) {
		return dir == Direction.LEFT || dir == Direction.RIGHT;
	}

	@Override
	public boolean getType() {
		return true;
	}
}
