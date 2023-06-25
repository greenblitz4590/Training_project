package edu.greenblitz.tobyDetermined.f2022;

import edu.greenblitz.tobyDetermined.commands.intake.roller.RunRoller;
import edu.greenblitz.utils.GBCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ShooterEvacuate extends ParallelRaceGroup {

	
	public ShooterEvacuate(){
		
		addCommands(
				new ShootByConstant(0.3), //todo use velocity control instead of voltage control
				new SequentialCommandGroup(
						new RunFunnel().alongWith(new RunRoller()).until(() ->HandleBalls.dm.get()),
						new WaitCommand(0.2),
						new ParallelRaceGroup(new RunFunnel(),new WaitCommand(0.3))
				)
		);
	}

}
