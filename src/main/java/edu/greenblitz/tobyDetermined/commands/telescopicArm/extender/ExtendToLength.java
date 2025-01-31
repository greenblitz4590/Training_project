package edu.greenblitz.tobyDetermined.commands.telescopicArm.extender;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtenderCommand;
import edu.greenblitz.tobyDetermined.subsystems.Battery;
import edu.greenblitz.tobyDetermined.subsystems.Console;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static edu.greenblitz.tobyDetermined.RobotMap.TelescopicArm.Extender.*;

public class ExtendToLength extends ExtenderCommand {

    private double legalGoalLength;
    private double wantedLength;
    private ProfiledPIDController pidController;

    public ExtendToLength(double length){
        wantedLength = length;
    }


    @Override
    public void initialize() {
        super.initialize();
        pidController = new ProfiledPIDController(PID.getKp(), PID.getKi(), PID.getKd(), CONSTRAINTS);
        SmartDashboard.putNumber("kp", SmartDashboard.getNumber("kp", pidController.getP()));
        SmartDashboard.putNumber("ki", SmartDashboard.getNumber("ki", pidController.getI()));
        SmartDashboard.putNumber("kd", SmartDashboard.getNumber("kd", pidController.getD()));
        SmartDashboard.putNumber("setpoint d", SmartDashboard.getNumber("setpoint d", 0));
        legalGoalLength = extender.getLegalGoalLength(wantedLength);
        pidController.reset(new TrapezoidProfile.State(extender.getLength(), extender.getVelocity()));
        lastSetpoint = 0;
    }
    private double lastSetpoint;
    @Override
    public void execute() {
        pidController.setP(SmartDashboard.getNumber("kp", pidController.getP()));
        pidController.setI(SmartDashboard.getNumber("ki", pidController.getI()));
        pidController.setD(SmartDashboard.getNumber("kd", pidController.getD()));
        double setpointD = SETPOINT_D;
        legalGoalLength = extender.getLegalGoalLength(wantedLength);
        pidController.setGoal(legalGoalLength);

        double setpointGain = (pidController.getSetpoint().velocity - lastSetpoint) * setpointD;
        lastSetpoint = pidController.getSetpoint().velocity;
        double pidGain = pidController.calculate(extender.getLength(), legalGoalLength) + setpointGain;
        double feedForward = Extender.getStaticFeedForward( Elbow.getInstance().getAngleRadians()) + Math.signum(pidGain) * kS;
        SmartDashboard.putNumber("vel target", pidController.getSetpoint().velocity);
        SmartDashboard.putNumber("pos target", pidController.getSetpoint().position);
        extender.setMotorVoltage(feedForward + pidGain);
    }

    @Override
    public boolean isFinished() {
        return extender.isAtLength(legalGoalLength);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        extender.stop();
    }
}