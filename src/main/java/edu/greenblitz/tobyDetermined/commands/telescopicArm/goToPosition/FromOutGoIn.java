package edu.greenblitz.tobyDetermined.commands.telescopicArm.goToPosition;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.elbow.RotateToAngle;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtendToLength;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class FromOutGoIn extends SequentialCommandGroup {

    protected FromOutGoIn(double lengthInMeters, double angleInRads){
        addCommands(
                new ExtendToLength(
                        Math.min(RobotMap.TelescopicArm.Extender.MAX_ENTRANCE_LENGTH, lengthInMeters))
                        .alongWith(new RotateToAngle(RobotMap.TelescopicArm.Elbow.END_WALL_ZONE_ANGLE))
        );
        addCommands(
                new RotateToAngle(angleInRads)
                        .alongWith(new ExtendToLength(lengthInMeters))
                        .beforeStarting(new WaitUntilCommand(() -> Elbow.getInstance().getState() == Elbow.ElbowState.WALL_ZONE)
                        )
        );
    }
}