package generation;

public class DummyOrder implements Order{
	
	//initialize
	private Builder builder;
	private int skill;
	private MazeConfiguration mazeConfiguration;
	private boolean perfect;
	int percentageDone;
	
	public DummyOrder(int skill, Builder builder, boolean perfect) {
		//set equal for instance
		this.skill = skill;
		this.builder = builder;
		this.perfect = perfect;
	}
	
	
	public MazeConfiguration getConfiguration() {
		return mazeConfiguration;
	}
	
	@Override
	public int getSkillLevel() {
		return skill;
	}
	@Override
	public Builder getBuilder() {
		return builder;
	}
	@Override
	public boolean isPerfect() {
		return perfect;
	}
	@Override
	public void deliver(MazeConfiguration mazeConfig) {
		this.mazeConfiguration = mazeConfig;
		
	}
	@Override
	public void updateProgress(int percentage) {
		this.percentageDone = percentage;
		
	}
	

}
