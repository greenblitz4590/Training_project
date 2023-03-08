package edu.greenblitz.tobyDetermined.commands.Auto;


import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.greenblitz.tobyDetermined.Field;
import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.commands.MultiSystem.FullIntake;
import edu.greenblitz.tobyDetermined.commands.MultiSystem.GripFromBelly;
import edu.greenblitz.tobyDetermined.commands.intake.extender.ExtendRoller;
import edu.greenblitz.tobyDetermined.commands.intake.extender.RetractRoller;
import edu.greenblitz.tobyDetermined.commands.intake.roller.RunRoller;
import edu.greenblitz.tobyDetermined.commands.intake.roller.StopRoller;
import edu.greenblitz.tobyDetermined.commands.rotatingBelly.RotateOutDoorDirection;
import edu.greenblitz.tobyDetermined.commands.swerve.MoveToPose;
import edu.greenblitz.tobyDetermined.commands.swerve.balance.bangBangBalance.FullBalance;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.claw.DropCone;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.claw.EjectCube;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.claw.GripBelly;
import edu.greenblitz.tobyDetermined.commands.telescopicArm.goToPosition.GoToPosition;
import edu.greenblitz.tobyDetermined.subsystems.LED;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SwerveChassis;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.HashMap;

public class PathFollowerBuilder extends SwerveAutoBuilder {

    /**
     * the use of the event map is to run a marker by its name run a command
     */
    private static final HashMap<String, Command> eventMap = new HashMap<>();
    public static final double DEADLINE_TIME_FOR_PRE_AUTO_COMMAND = 1;

    //todo add commands to event map
    static {
        eventMap.put("FullConeHighAndReturn", new FullConeHighAndReturn());
        eventMap.put("PlaceFromAdjacentConeHigh", new PlaceFromAdjacent(RobotMap.TelescopicArm.PresetPositions.CONE_HIGH));
        eventMap.put("PlaceFromAdjacentCubeHigh", new PlaceFromAdjacent(RobotMap.TelescopicArm.PresetPositions.CUBE_HIGH));
        eventMap.put("DropCone", new DropCone());
        eventMap.put("DropCube", new EjectCube());
        eventMap.put("ArmToBelly", new PlaceFromAdjacent(RobotMap.TelescopicArm.PresetPositions.INTAKE_GRAB_POSITION));
        eventMap.put("MoveToPose8", new MoveToPose(Field.PlacementLocations.getLocationsOnBlueSide()[7], true));
        eventMap.put("MoveToPose2", new MoveToPose(Field.PlacementLocations.getLocationsOnBlueSide()[1], true));
        eventMap.put("MoveToOutRamp", new MoveToPose(Field.PlacementLocations.OUT_PRE_BALANCE_BLUE, true));
        eventMap.put("BalanceFromOut", new FullBalance(true));
        eventMap.put("BalanceFromIn", new FullBalance(false));
        eventMap.put("gripFromFloor",new ExtendRoller().alongWith(
                new RunRoller(),
                new RotateOutDoorDirection()
        ).raceWith(new WaitCommand(1)).andThen(
                new StopRoller().alongWith(new RetractRoller()),
                new GripFromBelly()
                )
        
        );
        
        eventMap.put("led", new InstantCommand( () ->  LED.getInstance().setColor(Color.kOrange)));
    }

    private static PathFollowerBuilder instance;

    private PathFollowerBuilder() {
        super(
                SwerveChassis.getInstance()::getRobotPose,
                (Pose2d pose) -> SwerveChassis.getInstance().resetChassisPose(pose),
                SwerveChassis.getInstance().getKinematics(),
                RobotMap.Swerve.Frankenstein.TRANSLATION_PID,
                RobotMap.Swerve.Frankenstein.ROTATION_PID,
                SwerveChassis.getInstance()::setModuleStates,
                eventMap,
                true,
                SwerveChassis.getInstance());
    }


    public static PathFollowerBuilder getInstance() {
        if (instance == null) {
            instance = new PathFollowerBuilder();
        }
        return instance;
    }


    /**
     * @return returns the command for the full auto (commands and trajectory included)
     */
    public CommandBase followPath(String pathName) {

        PathPlannerTrajectory path;
        path = PathPlanner.loadPath(
                pathName, new PathConstraints(
                        RobotMap.Swerve.Frankenstein.MAX_VELOCITY,
                        RobotMap.Swerve.Frankenstein.MAX_ACCELERATION
                ));


        if (path == null) {
            SmartDashboard.putString("wrong pth", pathName);
        }
        //pathplanner was acting wierd when starting position was not the one defined in the path so i added a reset to the path start
        return fullAuto(path).beforeStarting(new SetToFirstTrajectoryState(path).raceWith(new WaitCommand(DEADLINE_TIME_FOR_PRE_AUTO_COMMAND)));
    }

    public static PathPlannerTrajectory getPathPlannerTrajectory(String path) {
        return PathPlanner.loadPath(path, new PathConstraints(RobotMap.Swerve.Frankenstein.MAX_VELOCITY, RobotMap.Swerve.Frankenstein.MAX_ACCELERATION));
    }

}