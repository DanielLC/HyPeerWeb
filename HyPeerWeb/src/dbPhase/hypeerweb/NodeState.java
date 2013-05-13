package dbPhase.hypeerweb;

public enum NodeState {
	DownPointingNodeState, HypercubeCapState, StandardNodeState,
	TerminalNodeState, UpPointingNodeState;
	
	public String toString() {
		switch(this) {
		case DownPointingNodeState:
			return "DownPointingNodeState";
		case HypercubeCapState:
			return "HypercubeCapState";
		case StandardNodeState:
			return "StandardNodeState";
		case TerminalNodeState:
			return "TerminalNodeState";
		case UpPointingNodeState:
			return "UpPointingNodeState";
		default:
			return "";
		}
	}
}
