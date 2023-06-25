package edu.greenblitz.tobyDetermined.f2022;

import edu.greenblitz.utils.GBCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class HandleBalls extends GBCommand {
	
	
	/**
	 * this command checks if an enemy ball is trying to enter the Funnel,
	 * and if so it is rejecting it from entering the funnel system using a
	 * color sensor to check the color
	 *
	 * @see com.revrobotics.ColorSensorV3 colorSensor.
	 * @see Indexing Subsystem
	 */
	
	private final Indexing index;
	private boolean isBallInFunnel;
	public static DigitalInput dm = new DigitalInput(0);
	public HandleBalls() {
		this.index = Indexing.getInstance();
		isBallInFunnel = false;
		require(Indexing.getInstance());
	}
	
	@Override
	public void execute() {
		SmartDashboard.putBoolean("isBallInFunnel", isBallInFunnel);
		SmartDashboard.putBoolean("isEnemyBallUnSensor", index.isEnemyBallInSensor());
		SmartDashboard.putBoolean("isMacroSwitch", dm.get());
		
		if (index.isTeamsBallInSensor()) {
			//if our team's ball is in front of the sensor activate the boolean
			isBallInFunnel = true;
		}
		if (dm.get()) {
			//if the ball got to the macroSwitch then disable the boolean
			isBallInFunnel = false;
		}
		
		if (index.isEnemyBallInSensor()) {
			if (dm.get() || isBallInFunnel) {
				isBallInFunnel = false;
				SmartDashboard.putBoolean("isEvacuatingFromShooter", false);
				//back direction
				new ParallelDeadlineGroup(
						new WaitCommand(0.5),
						new ReverseRunRoller(),
						new ReverseRunFunnel().raceWith(new WaitCommand(0.2))
				).andThen(new RunFunnel().until(() ->dm.get())).schedule();
			} else {
				//shooter evacuation
				SmartDashboard.putBoolean("isEvacuatingFromShooter", true);
				new ShooterEvacuate().raceWith(new WaitCommand(5)).schedule();
				
			}
		}
	}
}

