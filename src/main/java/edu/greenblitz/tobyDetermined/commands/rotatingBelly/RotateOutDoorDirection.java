package edu.greenblitz.tobyDetermined.commands.rotatingBelly;

import edu.greenblitz.tobyDetermined.RobotMap;

public class RotateOutDoorDirection extends RotateByPower {

    public RotateOutDoorDirection() {
        super(-RobotMap.RotatingBelly.ROTATING_POWER);
    }
    
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
