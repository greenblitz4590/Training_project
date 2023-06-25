package edu.greenblitz.tobyDetermined.f2022;


import edu.greenblitz.utils.GBCommand;

public class ReverseRunRoller extends GBCommand {
	
	public ReverseRunRoller(){
		require(Gripper.getInstance());
	}

	@Override
	public void execute() {
		Gripper.getInstance().setPower(-0.7);
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
	
	@Override
	public void end(boolean interrupted) {
		Gripper.getInstance().stop();
	}
}