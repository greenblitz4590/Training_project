package edu.greenblitz.tobyDetermined.commands.Shooter;

import edu.greenblitz.tobyDetermined.subsystems.shooter;
import edu.greenblitz.utils.GBCommand;

public class Extake extends GBCommand {
    @Override
    public void execute() {
        shooter.getInstance().setUpperMotorSpeed(0.3);
//        if (shooter.getInstance().timer.get() > 0.5) {
//            shooter.getInstance().setMiddleMotorSpeed(0.3);
//        }
        shooter.getInstance().setMiddleMotorSpeed(0.3);
// we start moving the upper motor
    }

    @Override
    public void end(boolean interrupted) {
        shooter.getInstance().setMiddleMotorSpeed(0);
        shooter.getInstance().setUpperMotorSpeed(0);
    }
}
