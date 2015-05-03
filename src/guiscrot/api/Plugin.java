package guiscrot.api;

public abstract class Plugin {
	
	private final String name;
	
	public Plugin(String name) {
		this.name = name;
	}
	
	/**
	 * Returns unique name for this plugin
	 * @return
	 */
	public String getName() {
		return this.name;
	}

}
