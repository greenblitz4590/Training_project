package edu.greenblitz.tobyDetermined.commands.swerve.measurement;

import edu.greenblitz.tobyDetermined.commands.swerve.SwerveCommand;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SwerveChassis;
import edu.greenblitz.utils.PIDObject;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalibratePID extends SwerveCommand {
	private PIDObject pid;

	public CalibratePID() {
		pid = new PIDObject(0.0, 0.0, 0.0);
	}

	@Override
	public void execute() {
		super.execute();
		double p = SmartDashboard.getNumber("P Gain", 0);
		double i = SmartDashboard.getNumber("I Gain", 0);
		double d = SmartDashboard.getNumber("D Gain", 0);
		double setPoint = SmartDashboard.getNumber("set point", 0);

		pid.setKp(p);
		pid.setKi(i);
		pid.setKd(d);

		SwerveChassis.getInstance().getModule(SwerveChassis.Module.BACK_LEFT).configLinPID(pid);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.BACK_LEFT).setLinSpeed(setPoint);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.BACK_RIGHT).configLinPID(pid);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.BACK_RIGHT).setLinSpeed(setPoint);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.FRONT_LEFT).configLinPID(pid);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.FRONT_LEFT).setLinSpeed(setPoint);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.FRONT_RIGHT).configLinPID(pid);
		SwerveChassis.getInstance().getModule(SwerveChassis.Module.FRONT_RIGHT).setLinSpeed(setPoint);
	}
}
