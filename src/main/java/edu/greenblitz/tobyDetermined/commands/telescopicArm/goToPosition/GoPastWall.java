package edu.greenblitz.tobyDetermined.commands.telescopicArm.goToPosition;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.elbow.RotateToAngleRadians;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtendToLength;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import java.util.function.BooleanSupplier;

public class GoPastWall extends ParallelDeadlineGroup {
    private double lengthInMeters, angleInRads;
    boolean passedWall = false;

    BooleanSupplier isFinished;

    protected GoPastWall(double lengthInMeters, double angleInRads) {
        super(new InstantCommand());
        this.lengthInMeters = lengthInMeters;
        this.angleInRads = angleInRads;
        isFinished = () -> Extender.getInstance().isAtLength(this.lengthInMeters)
                && Extender.getInstance().isNotMoving()
                && Elbow.getInstance().isAtAngle(this.angleInRads)
                && Elbow.getInstance().isNotMoving();
        setDeadline(new WaitUntilCommand(isFinished));
        addCommands(
                new ExtendToLength(RobotMap.TelescopicArm.Extender.MAX_ENTRANCE_LENGTH - RobotMap.TelescopicArm.Extender.LENGTH_TOLERANCE){
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }.raceWith(new WaitUntilCommand(()-> Elbow.getInstance().getState() == Elbow.ElbowState.WALL_ZONE))
                .andThen(
                        new ExtendToLength(lengthInMeters){
                            @Override
                            public boolean isFinished() {
                                return false;
                            }
                        }
                ),
                new RotateToAngleRadians(angleInRads){
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                });
    }


}
