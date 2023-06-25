package edu.greenblitz.tobyDetermined.commands.intake.roller;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.f2022.Gripper;
import edu.greenblitz.tobyDetermined.f2022.RunFunnel;
import edu.greenblitz.utils.GBCommand;

public class RunRoller extends GBCommand {
	
	public RunRoller(){
		require(Gripper.getInstance());
	}
	
	@Override
	public void execute() {
		Gripper.getInstance().setPower(1);
	}

	@Override
	public void end(boolean interrupted) {
		Gripper.getInstance().stop();
	}
}
