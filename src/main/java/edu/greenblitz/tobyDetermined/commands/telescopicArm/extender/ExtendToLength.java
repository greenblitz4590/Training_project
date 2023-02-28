package edu.greenblitz.tobyDetermined.commands.telescopicArm.extender;

import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;

import static edu.greenblitz.tobyDetermined.RobotMap.TelescopicArm.Extender.CONSTRAINTS;

public class ExtendToLength extends ExtenderCommand {

    private double legalGoalLength;
    private double wantedLength;
    private TrapezoidProfile trapezoidProfile;
    private Timer timer;

    public ExtendToLength(double length){
        wantedLength = length;
        timer = new Timer();
    }

    @Override
    public void initialize() {
        super.initialize();
        legalGoalLength = extender.getLegalGoalLength(wantedLength);
        trapezoidProfile = new TrapezoidProfile(CONSTRAINTS,new TrapezoidProfile.State(legalGoalLength, 0), new TrapezoidProfile.State(extender.getLength(), extender.getVelocity()));
        timer.restart();
    }

    @Override
    public void execute() {
        if (legalGoalLength != extender.getLegalGoalLength(wantedLength)){
            legalGoalLength = extender.getLegalGoalLength(wantedLength);
            trapezoidProfile = new TrapezoidProfile(CONSTRAINTS,new TrapezoidProfile.State(legalGoalLength, 0), new TrapezoidProfile.State(extender.getLength(), extender.getVelocity()));
            timer.restart();
        }
        TrapezoidProfile.State setpoint = trapezoidProfile.calculate(timer.get());
        double feedForward = Extender.getStaticFeedForward( Elbow.getInstance().getAngleRadians());
        extender.moveTowardsLength(legalGoalLength, feedForward);//setpoint.position, feedForward);
    }

    @Override
    public boolean isFinished() {
        return extender.isAtLength(legalGoalLength);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
