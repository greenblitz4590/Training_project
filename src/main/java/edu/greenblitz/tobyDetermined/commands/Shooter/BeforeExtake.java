package edu.greenblitz.tobyDetermined.commands.Shooter;

import edu.greenblitz.tobyDetermined.subsystems.shooter;
import edu.greenblitz.utils.GBCommand;

public class BeforeExtake extends GBCommand {
    @Override
    public void execute() {
        shooter.getInstance().timer.restart();

    }

    @Override
    public void end(boolean interrupted) {

    }
}
