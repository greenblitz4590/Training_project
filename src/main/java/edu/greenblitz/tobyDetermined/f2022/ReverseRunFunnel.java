package edu.greenblitz.tobyDetermined.f2022;


import edu.greenblitz.utils.GBCommand;

public class ReverseRunFunnel extends GBCommand {
	public ReverseRunFunnel(){
		require(Funnel.getInstance());
	}
	@Override
	public void execute() {
		Funnel.getInstance().moveMotor(true);
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
