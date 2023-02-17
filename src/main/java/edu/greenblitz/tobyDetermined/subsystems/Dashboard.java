package edu.greenblitz.tobyDetermined.subsystems;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.commands.swerve.MoveToGrid.Grid;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.elbow.RotateToAngle;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtendToLength;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SwerveChassis;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Elbow;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Map;

public class Dashboard extends GBSubsystem {

	private static Dashboard instance;

	public static Dashboard getInstance() {
		if (instance == null) {
			init();
			SmartDashboard.putBoolean("dashboard initialized via getinstance", true);
		}
		return instance;
	}

	public static void init() {
		instance = new Dashboard();
	}

	private Dashboard() {
		driversDashboard();
		swerveDashboard();
		armDashboard();
	}

	@Override
	public void periodic() {

	}


	public void driversDashboard() {
		ShuffleboardTab driversTab = Shuffleboard.getTab("Drivers");

		//arm states
		ShuffleboardLayout armStateWidget = driversTab.getLayout("Arm states", BuiltInLayouts.kGrid)
				.withPosition(0, 0).withSize(2, 2).withProperties(Map.of("Label position", "TOP", "Number of columns", 2, "Number of rows", 2));
		armStateWidget.addString("Extender State", () -> Extender.getInstance().getState().toString()).withPosition(0, 0);
		armStateWidget.addDouble("Length", () -> Extender.getInstance().getLength()).withPosition(1, 0);
		armStateWidget.addString("Elbow State", () -> Elbow.getInstance().getState().toString()).withPosition(0, 1);
		armStateWidget.addDouble("Angle", () -> Elbow.getInstance().getAngle()).withPosition(1, 1);

		//arm state
		driversTab.addString("Arm state", () -> "doesn't exist").withPosition(4, 2).withSize(1, 2);

		//grid todo make it mirror by alliance
		ShuffleboardLayout grid = driversTab.getLayout("Grid", BuiltInLayouts.kGrid)
				.withPosition(2, 0).withSize(6, 2).withProperties(Map.of("Label position", "TOP", "Number of columns", 9, "Number of rows", 3));
		for (int i = 0; i < 9; i++) {
			for (Grid.Height height : Grid.Height.values()) {
				int finalI = i;
				int finalHeight = height.ordinal();
				grid.addBoolean(i + 1 + " " + height, () -> (Grid.getInstance().getSelectedHeightID() == finalHeight && Grid.getInstance().getSelectedPositionID() == finalI))
						.withPosition(finalI, 2 - finalHeight);
			}
		}

		//pose
		ShuffleboardLayout robotPoseWidget = driversTab.getLayout("Robot pose", BuiltInLayouts.kList)
				.withPosition(0, 2).withSize(1, 2).withProperties(Map.of("Label position", "TOP"));
		robotPoseWidget.addDouble("X", () -> SwerveChassis.getInstance().getRobotPose().getX());
		robotPoseWidget.addDouble("Y", () -> SwerveChassis.getInstance().getRobotPose().getY());
		robotPoseWidget.addDouble("Rotation", () -> SwerveChassis.getInstance().getRobotPose().getRotation().getDegrees());

		//battery
		driversTab.addDouble("Battery", () -> Battery.getInstance().getCurrentVoltage())
				.withPosition(9, 3);

		//object inside
		driversTab.addString("Object inside", /*()->RotatingBelly.getInstance().getGameObject().toString()*/() -> "robot does not exist")
				.withSize(1, 1).withPosition(2, 2);

		//field
		driversTab.add("Field", SwerveChassis.getInstance().getField()).withPosition(5, 2).withSize(3, 2);


		//console
		ShuffleboardLayout console =Console.getShuffleboardConsole(driversTab)
				.withPosition(8,0).withSize(2,3).withProperties(Map.of("Label position", "TOP"));


		//ready to place
		driversTab.addBoolean("Ready to place", () -> false).withPosition(3, 2).withSize(1, 2);
		//todo check if at place and arm in pos


	}

	public void armDashboard() {
		ShuffleboardTab armTab = Shuffleboard.getTab("Arm debug");

		//arm states
		ShuffleboardLayout armStateWidget = armTab.getLayout("Arm states", BuiltInLayouts.kGrid)
				.withPosition(0, 0).withSize(2, 2).withProperties(Map.of("Label position", "TOP", "Number of columns", 2, "Number of rows", 2));
		armStateWidget.addString("Extender State", () -> Extender.getInstance().getState().toString()).withPosition(0, 0);
		armStateWidget.addDouble("Length", () -> Extender.getInstance().getLength()).withPosition(1, 0);
		armStateWidget.addString("Elbow State", () -> Elbow.getInstance().getState().toString()).withPosition(0, 1);
		armStateWidget.addDouble("Angle", () -> Elbow.getInstance().getAngle()).withPosition(1, 1);

		//arm state
		armTab.addString("Arm state", () -> "doesn't exist").withPosition(4, 2).withSize(1, 2);


		//extender length
		armTab.addDouble("Extender length", () -> Extender.getInstance().getLength());

		//extender state
		armTab.addString("Extender state", () -> String.valueOf(Extender.getInstance().getState()));

		//extender ff
		armTab.addDouble("Extender ff", () -> Extender.getInstance().getFeedForward());

		//moves to length from dashboard input
		GenericEntry length = armTab.add("Desired length", 0).getEntry();
		new ExtendToLength(length.getDouble(0)).schedule();

		//set extender kp
		GenericEntry extenderKp = armTab.add("Desired kp extender", RobotMap.telescopicArm.extender.PID.getKp()).getEntry();

		//set extender ki
		GenericEntry extenderKi = armTab.add("Desired ki extender", RobotMap.telescopicArm.extender.PID.getKi()).getEntry();

		//set extender kd
		GenericEntry extenderKd = armTab.add("Desired kd extender", RobotMap.telescopicArm.extender.PID.getKd()).getEntry();

		Extender.getInstance().setPID(extenderKp.getDouble(0), extenderKi.getDouble(0), extenderKd.getDouble(0));


		//elbow angle
		armTab.addDouble("Elbow angle", ()-> Elbow.getInstance().getAngle());

		//elbow state
		armTab.addString("Elbow state", ()-> String.valueOf(Elbow.getInstance().getState()));

		//elbow ff
		armTab.addDouble("elbow ff", ()-> Elbow.getInstance().getCalculatedFeedForward());

		//moves to length from dashboard input
		GenericEntry angle = armTab.add("Desired angle", 0).getEntry();
		new RotateToAngle(length.getDouble(0));

		//set elbow kp
		GenericEntry elbowKp = armTab.add("Desired kp elbow", RobotMap.telescopicArm.elbow.PID.getKp()).getEntry();

		//set elbow ki
		GenericEntry elbowKi = armTab.add("Desired ki elbow", RobotMap.telescopicArm.elbow.PID.getKi()).getEntry();

		//set elbow kd
		GenericEntry elbowKd = armTab.add("Desired kd elbow", RobotMap.telescopicArm.elbow.PID.getKd()).getEntry();

		Elbow.getInstance().setPID(elbowKp.getDouble(0), elbowKi.getDouble(0), elbowKd.getDouble(0));
	}

	public void swerveDashboard() {
		ShuffleboardTab swerveTab = Shuffleboard.getTab("Swerve");
		for (SwerveChassis.Module module : SwerveChassis.Module.values()) {
			swerveTab.addDouble(module + "-angle", () -> Math.IEEEremainder(Math.toDegrees(SwerveChassis.getInstance().getModuleAngle(module)), 360))
					.withSize(2, 1).withPosition(module.ordinal() * 2, 0);
			swerveTab.addDouble(module + "-absolute-angle", () -> SwerveChassis.getInstance().getModuleAbsoluteEncoderValue(module))
					.withSize(2, 1).withPosition(module.ordinal() * 2, 1);
		}
		swerveTab.addDouble("pigeon-angle", () -> Math.toDegrees(SwerveChassis.getInstance().getChassisAngle()))
				.withSize(1, 1).withPosition(0, 2);

	}
}
