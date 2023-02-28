package edu.greenblitz.tobyDetermined.commands.telescopicArm.goToPosition;

import edu.greenblitz.tobyDetermined.commands.telescopicArm.elbow.RotateToAngleRadians;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtendToLength;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.greenblitz.utils.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import java.util.function.BooleanSupplier;

public class SimpleGoToPosition extends ParallelDeadlineGroup {

    private double lengthInMeters, angleInRads;

    BooleanSupplier isFinished;

    protected SimpleGoToPosition(double lengthInMeters, double angleInRads) {
        super(new InstantCommand());
        this.lengthInMeters = lengthInMeters;
        this.angleInRads = angleInRads;
        isFinished = () -> Extender.getInstance().isAtLength(this.lengthInMeters)
            && Extender.getInstance().isNotMoving()
            && Elbow.getInstance().isAtAngle(this.angleInRads)
            && Elbow.getInstance().isNotMoving();
        setDeadline(new WaitUntilCommand(isFinished));
        addCommands(
                new ExtendToLength(lengthInMeters){
                        @Override
                        public boolean isFinished() {
                            return false;
                        }
                    },
                new RotateToAngleRadians(angleInRads){
                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                });
    }


}
