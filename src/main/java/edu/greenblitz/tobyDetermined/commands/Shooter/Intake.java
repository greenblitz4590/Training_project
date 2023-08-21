package edu.greenblitz.tobyDetermined.commands.Shooter;

import edu.greenblitz.tobyDetermined.subsystems.shooter;
import edu.greenblitz.utils.GBCommand;

public class Intake extends GBCommand {

    @Override
    public void execute() {
        shooter.getInstance().setLowerMotorSpeed(0.8);

    }

    @Override
    public void end(boolean interrupted) {
        shooter.getInstance().setLowerMotorSpeed(0);
    }
}
