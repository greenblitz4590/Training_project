package edu.greenblitz.tobyDetermined.f2022;

import edu.greenblitz.utils.GBCommand;

public class RunFunnel extends GBCommand {
	public RunFunnel(){
		require(Funnel.getInstance());
	}
	@Override
	public void execute() {
		Funnel.getInstance().moveMotor(false);
	}
	
	@Override
	public void end(boolean interrupted) {
		Funnel.getInstance().stopMotor();
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
}
