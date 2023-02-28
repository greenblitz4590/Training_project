package edu.greenblitz.tobyDetermined.commands.telescopicArm.goToPosition;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;

public class GoToWallZone extends SimpleGoToPosition{

	protected GoToWallZone() {
		super(RobotMap.TelescopicArm.Extender.BACKWARDS_LIMIT, (RobotMap.TelescopicArm.Elbow.END_WALL_ZONE_ANGLE+RobotMap.TelescopicArm.Elbow.STARTING_WALL_ZONE_ANGLE) /2);
		super.isFinished = () -> Extender.getInstance().getState() == Extender.ExtenderState.IN_WALL_LENGTH
				&& Elbow.getInstance().getState() == Elbow.ElbowState.WALL_ZONE;
	}
}
