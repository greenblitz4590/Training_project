package edu.greenblitz.tobyDetermined.commands.Shooter;

import edu.greenblitz.utils.GBCommand;
import edu.greenblitz.tobyDetermined.subsystems.shooter;

public class ShooterC extends GBCommand {

    @Override
    public void execute() {
        shooter.getInstance().setMiddleMotorSpeed(-0.6);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.getInstance().setMiddleMotorSpeed(0);
    }
}
