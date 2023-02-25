package edu.greenblitz.tobyDetermined.commands.telescopicArm.elbow;

import edu.greenblitz.tobyDetermined.subsystems.Console;
import edu.wpi.first.wpilibj.Timer;

public class DebugFindAcc extends ElbowCommand{
	double currVelocity;
	double lastVelocity;
	Timer time = new Timer();
	double maxAccel;
	double voltage;
	public DebugFindAcc(double voltage){
		this.voltage = voltage;
	}

	@Override
	public void initialize() {
		super.initialize();
		lastVelocity =0;
		maxAccel  = 0;
		time.stop();
		time.start();
	}

	@Override
	public void execute() {
		elbow.setMotorVoltage(voltage);
		currVelocity = elbow.getVelocity();
		if (time.advanceIfElapsed(0.1)) {
			double accel = (currVelocity - lastVelocity);
			lastVelocity = elbow.getVelocity();
			maxAccel = Math.max(accel, maxAccel);
		}

	}

	@Override
	public void end(boolean interrupted) {
		Console.log("max Accel", Double.toString(maxAccel));
		elbow.setMotorVoltage(0);
	}
}
