package edu.greenblitz.tobyDetermined.commands.swerve.MoveToGrid;

import edu.greenblitz.tobyDetermined.commands.swerve.MoveToPose;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SwerveChassis;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * moves to a selected location from a list found in grid
 * using moveToPose
 * */
public class MoveToGrid extends MoveToPose {
    public MoveToGrid(){
        super(new Pose2d()); //will be overridden
    }

    @Override
    public void initialize() {
        pose = Grid.getInstance().getSelectedPosition();
        new InstantCommand(()-> SwerveChassis.getInstance().resetToVision());
        super.initialize();
    }
}
