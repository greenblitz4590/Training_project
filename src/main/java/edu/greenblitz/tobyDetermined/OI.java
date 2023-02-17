package edu.greenblitz.tobyDetermined;

import edu.greenblitz.tobyDetermined.commands.LED.*;
import edu.greenblitz.tobyDetermined.commands.indicatingCommands.BalanceOnRampWithIndicatorForward;
import edu.greenblitz.tobyDetermined.commands.swerve.CombineJoystickMovement;
import edu.greenblitz.tobyDetermined.commands.swerve.MoveToGrid.MoveToGrid;
import edu.greenblitz.tobyDetermined.commands.swerve.MoveToGrid.MoveSelectedTargetLeft;
import edu.greenblitz.tobyDetermined.commands.swerve.MoveToGrid.MoveSelectedTargetRight;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SwerveChassis;
import edu.greenblitz.utils.hid.SmartJoystick;
import edu.greenblitz.tobyDetermined.commands.intake.extender.ExtendRoller;
import edu.greenblitz.tobyDetermined.commands.intake.extender.RetractRoller;
import edu.greenblitz.tobyDetermined.commands.intake.extender.ToggleRoller;
import edu.greenblitz.tobyDetermined.commands.swerve.ToggleBrakeCoast;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class OI { //GEVALD

	private static OI instance;
	private static boolean isHandled = true;
	private final SmartJoystick mainJoystick;

	private final SmartJoystick secondJoystick;


	private OI() {
		mainJoystick = new SmartJoystick(RobotMap.Joystick.MAIN, 0.1);
		secondJoystick = new SmartJoystick(RobotMap.Joystick.SECOND, 0.2);
		initButtons();

	}

	public static OI getInstance() {
		if (instance == null) {
			init();
			SmartDashboard.putBoolean("oi initialized via getinstance", true);
		}
		return instance;
	}

	public static void init() {
		instance = new OI();
	}

	public static boolean isIsHandled() {
		return isHandled;
	}

	public static void disableHandling() {
		isHandled = false;
	}

	public double countB = 0;


	private void initButtons() {

		mainJoystick.POV_DOWN.onTrue(new HumanPlayerObjectIndicator(HumanPlayerObjectIndicator.wantedObject.CUBE));
		mainJoystick.A.onTrue(new HumanPlayerObjectIndicator(HumanPlayerObjectIndicator.wantedObject.CONE));
		mainJoystick.B.onTrue(new ObjectInIntakeLED());
		mainJoystick.START.onTrue(new ObjectInClawLED());
		mainJoystick.Y.onTrue(new BalanceOnRampLED());
		mainJoystick.POV_LEFT.whileTrue(new BalanceOnRampWithIndicatorForward(true));

	}

	public SmartJoystick getMainJoystick() {
		return mainJoystick;
	}

	public SmartJoystick getSecondJoystick() {
		return secondJoystick;
	}
}

