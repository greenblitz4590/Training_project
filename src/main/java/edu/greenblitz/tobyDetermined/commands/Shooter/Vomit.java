package edu.greenblitz.tobyDetermined.commands.Shooter;

import edu.greenblitz.tobyDetermined.subsystems.shooter;
import edu.greenblitz.utils.GBCommand;

public class Vomit extends GBCommand {
    @Override
    public void execute() {
        shooter.getInstance().speedModifier *= -1;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
