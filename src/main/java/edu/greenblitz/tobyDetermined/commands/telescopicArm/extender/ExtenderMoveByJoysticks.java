package edu.greenblitz.tobyDetermined.commands.telescopicArm.extender;

import edu.greenblitz.tobyDetermined.subsystems.Battery;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.greenblitz.utils.hid.SmartJoystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ExtenderMoveByJoysticks extends ExtenderCommand {
	private SmartJoystick joystick;
	public ExtenderMoveByJoysticks(SmartJoystick joystick) {
		super();
		this.joystick = joystick;
	}

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public void execute() {
		double power = joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y) * 1;
		double ff = Extender.getStaticFeedForward(Elbow.getInstance().getAngleRadians());
		SmartDashboard.putNumber("joystick + ff volt",power * Battery.getInstance().getCurrentVoltage() + ff);

		SmartDashboard.putNumber("ext velocity", extender.getVelocity());

		
		extender.debugSetPower((power * Battery.getInstance().getCurrentVoltage() + ff) / Battery.getInstance().getCurrentVoltage());
	}

	@Override
	public void end(boolean interrupted) {
		super.end(interrupted);
		extender.stop();
	}
}
