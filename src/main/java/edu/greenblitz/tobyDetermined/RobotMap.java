package edu.greenblitz.tobyDetermined;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.pathplanner.lib.auto.PIDConstants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.greenblitz.tobyDetermined.subsystems.swerve.KazaSwerveModule;
import edu.greenblitz.tobyDetermined.subsystems.swerve.SdsSwerveModule;
import edu.greenblitz.utils.PIDObject;
import edu.greenblitz.utils.motors.GBFalcon;
import edu.greenblitz.utils.motors.GBSparkMax;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import static edu.greenblitz.tobyDetermined.RobotMap.TelescopicArm.Elbow.*;

public class RobotMap {
    public static final Robot.robotName ROBOT_NAME = Robot.robotName.Frankenstein;

    public static class General {

        public final static double MIN_VOLTAGE_BATTERY = 11.97;
        public final static double VOLTAGE_COMP_VAL = 11.5;
        public final static double RAMP_RATE_VAL = 0.4;

        public static class Motors {

            public final static double SPARKMAX_TICKS_PER_RADIAN = Math.PI * 2;
            public final static double SPARKMAX_VELOCITY_UNITS_PER_RPM = 1;
            public static final double NEO_PHYSICAL_TICKS_TO_RADIANS = SPARKMAX_TICKS_PER_RADIAN / 42; //do not use unless you understand the meaning
            public final static double FALCON_TICKS_PER_RADIAN = 2 * Math.PI / 2048.0;

            public final static double FALCON_VELOCITY_UNITS_PER_RPM = 600.0 / 2048;
        }
    }


    public static class gyro { //Yum
        public static final int pigeonID = 12;
    }

    public static class Joystick {
        public static final int MAIN = 0;
        public static final int SECOND = 1;
    }

    public static class Pneumatics {
        public static class PCM {
            public static final int PCM_ID = 22;
            public static final PneumaticsModuleType PCM_TYPE = PneumaticsModuleType.CTREPCM;
        }

        public static class PressureSensor {
            public static final int PRESSURE = 3;
        }
    }

    public static class Vision {
        public static final String[] LIMELIGHT_NAMES = new String[]{"limelight-front"};
        public static double STANDARD_DEVIATION_ODOMETRY = 0.001;
        public static double STANDARD_DEVIATION_VISION2D = 0.3;
        public static double STANDARD_DEVIATION_VISION_ANGLE = 0.1;
        public static final int[] PORT_NUMBERS = {5800, 5801, 5802, 5803, 5804, 5805};
        public static final Transform3d ROBOT_TO_CAMERA = new Transform3d(new Translation3d(), new Rotation3d());
        public final static double MIN_DISTANCE_TO_FILTER_OUT = 1;


    }

    public static class LED {

	    public static final int LENGTH = 100;
	    public static final int PORT = 0;
        public static final double BLINKING_TIME = 0.2;
    }

    public static class Ultrasonic {
        public static final int PING_DIO_PORT =5;
        public static final int ECHO_DIO_PORT = 6;
        public static final double DISTANCE_FROM_FLOOR_TO_STOP_IN_MM = 120;
    }

    public static class Swerve {

        public static class Frankenstein {


            public static final double ROBOT_LENGTH_IN_METERS = 0.69;
            public static final double ROBOT_WIDTH_IN_METERS = 0.79;

            public static final double MAX_VELOCITY = 4;
            public static final double MAX_ACCELERATION = 4;
            public static final double MAX_ANGULAR_SPEED = 8;
            public static final double MAX_ANGULAR_ACCELERATION = 20; //todo calibrate
            public static final int LAMPREY_AVERAGE_BITS = 2;


            public static final PIDConstants TRANSLATION_PID = new PIDConstants(2, 0, 0);

            public static final PIDConstants ROTATION_PID = new PIDConstants(2, 0, 0);

            public static final Translation2d[] SwerveLocationsInSwerveKinematicsCoordinates = new Translation2d[]{
                    //the WPILib coordinate system is stupid. (x is forwards, y is leftwards)
                    //the translations are given rotated by 90 degrees clockwise to avoid coordinates system conversion at output
                    new Translation2d(0.3020647, 0.25265), /*fl*/
                    new Translation2d(0.3020647, -0.25265),/*fr*/
                    new Translation2d(-0.3020647, 0.25265),/*bl*/
                    new Translation2d(-0.3020647, -0.25265)/*br*/
            };
        }


        static final Pose2d initialRobotPosition = new Pose2d(0, 0, new Rotation2d(0));
        public static final Translation2d[] SwerveLocationsInSwerveKinematicsCoordinates = new Translation2d[]{
                //the WPILib coordinate system is stupid. (x is forwards, y is leftwards)
                //the translations are given rotated by 90 degrees clockwise to avoid coordinates system conversion at output
                new Translation2d(0.3020647, 0.25265), /*fl*/
                new Translation2d(0.3020647, -0.25265),/*fr*/
                new Translation2d(-0.3020647, 0.25265),/*bl*/
                new Translation2d(-0.3020647, -0.25265)/*br*/};

        public static final double MAX_VELOCITY = 4.1818320981472068;
        public static final double MAX_ACCELERATION = 14.37979171376739;
        public static final double MAX_ANGULAR_SPEED = 10.454580245368017;


        public static final PIDObject rotationPID = new PIDObject().withKp(0.5).withKi(0).withKd(0).withFF(0.1);
        public static final double ks = 0.14876;
        public static final double kv = 3.3055;

        public static final double ka = 0.11023;

        public static KazaSwerveModule.KazaSwerveModuleConfigObject KazaModuleFrontLeft = new KazaSwerveModule.KazaSwerveModuleConfigObject(1, 10, 0, false); //front left

        public static KazaSwerveModule.KazaSwerveModuleConfigObject KazaModuleFrontRight = new KazaSwerveModule.KazaSwerveModuleConfigObject(3, 11, 2, true); //front right

        public static KazaSwerveModule.KazaSwerveModuleConfigObject KazaModuleBackLeft = new KazaSwerveModule.KazaSwerveModuleConfigObject(2, 8, 1, false); //back left

        public static KazaSwerveModule.KazaSwerveModuleConfigObject KazaModuleBackRight = new KazaSwerveModule.KazaSwerveModuleConfigObject(12, 5, 3, true); //back right

        public static SdsSwerveModule.SdsSwerveModuleConfigObject SdsModuleFrontLeft = new SdsSwerveModule.SdsSwerveModuleConfigObject(1, 0, 0, false, 0.863 ); //front left

        public static SdsSwerveModule.SdsSwerveModuleConfigObject SdsModuleFrontRight = new SdsSwerveModule.SdsSwerveModuleConfigObject(3, 2, 1, true, 0.303 ); //front right


        public static SdsSwerveModule.SdsSwerveModuleConfigObject SdsModuleBackLeft = new SdsSwerveModule.SdsSwerveModuleConfigObject(5, 4, 2, false, 0.726); //back left

        public static SdsSwerveModule.SdsSwerveModuleConfigObject SdsModuleBackRight = new SdsSwerveModule.SdsSwerveModuleConfigObject(7, 6, 3, true, 0.885); //back right


        public static class KazaSwerve {
            public static final double ANG_GEAR_RATIO = 6.0;
            public static final double LIN_GEAR_RATIO = 8.0;

            public static final double ks = 0.14876;
            public static final double kv = 3.3055;
            public static final double ka = 0.11023;

            public static final double WHEEL_CIRC = 0.0517 * 2 * Math.PI;
            public static final double linTicksToMeters = RobotMap.General.Motors.SPARKMAX_TICKS_PER_RADIAN * WHEEL_CIRC / LIN_GEAR_RATIO / (2 * Math.PI);
            public static final double angleTicksToWheelToRPM = RobotMap.General.Motors.SPARKMAX_VELOCITY_UNITS_PER_RPM / ANG_GEAR_RATIO;
            public static final double linTicksToMetersPerSecond = RobotMap.General.Motors.SPARKMAX_VELOCITY_UNITS_PER_RPM * WHEEL_CIRC / 60 / LIN_GEAR_RATIO;
            public static final PIDObject linPID = new PIDObject().withKp(0.0003).withMaxPower(0.5);
            public static final GBSparkMax.SparkMaxConfObject baseLinConfObj = new GBSparkMax.SparkMaxConfObject().withIdleMode(CANSparkMax.IdleMode.kBrake).withCurrentLimit(40).withRampRate(RobotMap.General.RAMP_RATE_VAL).withVoltageComp(RobotMap.General.VOLTAGE_COMP_VAL).withPID(linPID).withPositionConversionFactor(linTicksToMeters).withVelocityConversionFactor(linTicksToMetersPerSecond);
            public static final double angleTicksToRadians = RobotMap.General.Motors.SPARKMAX_TICKS_PER_RADIAN / ANG_GEAR_RATIO;
            public static final PIDObject angPID = new PIDObject().withKp(0.5).withMaxPower(1.0);
            public static final GBSparkMax.SparkMaxConfObject baseAngConfObj = new GBSparkMax.SparkMaxConfObject().withIdleMode(CANSparkMax.IdleMode.kBrake).withCurrentLimit(30).withRampRate(RobotMap.General.RAMP_RATE_VAL).withVoltageComp(RobotMap.General.VOLTAGE_COMP_VAL).withInverted(true).withPID(angPID).withPositionConversionFactor(angleTicksToRadians).withVelocityConversionFactor(angleTicksToWheelToRPM);
        }

        public static class SdsSwerve {
            public static final double ANG_GEAR_RATIO = (150.0 / 7);
            public static final double LIN_GEAR_RATIO = 8.14;

            public static final double ks = 0.16411;
            public static final double kv = 2.6824;
            public static final double ka = 0.25968;

            public static final double WHEEL_CIRC = 0.0517 * 2 * Math.PI;
            public static final double linTicksToMeters = RobotMap.General.Motors.FALCON_TICKS_PER_RADIAN * WHEEL_CIRC / 2 / Math.PI / LIN_GEAR_RATIO;
            public static final double angleTicksToWheelToRPM = RobotMap.General.Motors.FALCON_VELOCITY_UNITS_PER_RPM / ANG_GEAR_RATIO;
            public static final double linTicksToMetersPerSecond = RobotMap.General.Motors.FALCON_VELOCITY_UNITS_PER_RPM / LIN_GEAR_RATIO * WHEEL_CIRC / 60;
            public static final double angleTicksToRadians = RobotMap.General.Motors.FALCON_TICKS_PER_RADIAN / ANG_GEAR_RATIO;
            public static final double magEncoderTicksToFalconTicks = 2 * Math.PI / angleTicksToRadians;

            public static final PIDObject angPID = new PIDObject().withKp(0.05).withMaxPower(1.0).withFF(0);//.withKd(10).withMaxPower(0.8);
            public static final GBFalcon.FalconConfObject baseAngConfObj = new GBFalcon.FalconConfObject().withNeutralMode(NeutralMode.Brake).withCurrentLimit(30).withRampRate(RobotMap.General.RAMP_RATE_VAL).withVoltageCompSaturation(RobotMap.General.VOLTAGE_COMP_VAL).withInverted(true).withPID(angPID);

            public static final PIDObject linPID = new PIDObject().withKp(0.0003).withMaxPower(0.5);
            public static final GBFalcon.FalconConfObject baseLinConfObj = new GBFalcon.FalconConfObject().withNeutralMode(NeutralMode.Brake).withCurrentLimit(40).withRampRate(RobotMap.General.RAMP_RATE_VAL).withVoltageCompSaturation(RobotMap.General.VOLTAGE_COMP_VAL).withPID(linPID);
        }

        public static class Autonomus {
            public static final TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(MAX_VELOCITY, MAX_ACCELERATION);
        }

    }

    public static class RotatingBelly {
        public static final int MOTOR_ID = 15;
        public static final double ROTATING_TIME = 0.8;
        public static final double ROTATING_POWER = 0.8;
        public static final int MACRO_SWITCH_DIO_PORT = 7;

        public static final double ROTATE_OUT_OF_DOOR_TIME = 0.5;
        public static double ROTATE_TO_DOOR_TIME = 3;

        public static final int CLOSE_PISTON_ID = 0;
        public static final int OPEN_PISTON_ID = 0;

    }

    public static class TelescopicArm {


        public enum PresetPositions {
            //height in meters
            //input angle degrees output angle radians
            CONE_HIGH(0.795,  Math.toRadians(25.1) - STARTING_ANGLE_RELATIVE_TO_GROUND),
            CONE_MID(0.29,  1.94),
            CUBE_HIGH(0.654,   Math.toRadians(20.7) - STARTING_ANGLE_RELATIVE_TO_GROUND),
            CUBE_MID( 0.450,  Math.toRadians(15.46) - STARTING_ANGLE_RELATIVE_TO_GROUND),
            LOW(0.35,  Math.toRadians(60)),
            
            COMMUNITY_PRE_GRID(Extender.MAX_ENTRANCE_LENGTH - Extender.LENGTH_TOLERANCE, CONE_HIGH.angleInRadians),

            INTAKE_GRAB_POSITION(0.28, 0.18),
            PRE_INTAKE_GRAB_POSITION(0,
                    0.28),

            ZIG_HAIL(0,Math.toRadians(20.7) - STARTING_ANGLE_RELATIVE_TO_GROUND),
            FEEDER(0.663,1.949);


            public double distance;
            public double angleInRadians;

            PresetPositions(double distance, double angleInRads) {
                this.distance = distance;
                this.angleInRadians = angleInRads;

            }
        }

        public static class Extender {
            public static final int MOTOR_ID = 3;
            public static final double GEAR_RATIO = 1/5.0;
            public static final double STARTING_LENGTH =0.3;
            public static final int BACKWARDS_LIMIT = 0;
            public static final double FORWARD_LIMIT = 0.8;
            public static final double EXTENDED_LENGTH = FORWARD_LIMIT- 0.05;
            public static final SparkMaxLimitSwitch.Type SWITCH_TYPE = SparkMaxLimitSwitch.Type.kNormallyClosed;
            public static final double MAX_LENGTH_IN_ROBOT = 0.37;
            public static final double MAX_ENTRANCE_LENGTH = 0.054;
            public static final PIDObject PID = new PIDObject().withKp(10).withKi(0.05).withMaxPower(1);
            public static final double DEBOUNCE_TIME_FOR_LIMIT_SWITCH = 0.05;
            

            public static final double EXTENDER_EXTENDING_GEAR_CIRC = 0.0165 * (2 * Math.PI);
            public static final double POSITION_CONVERSION_FACTOR = GEAR_RATIO * EXTENDER_EXTENDING_GEAR_CIRC;
            public static final double VELOCITY_CONVERSION_FACTOR = POSITION_CONVERSION_FACTOR / 60;
            public static final double LENGTH_TOLERANCE = 0.03; //in meters
            public static final double FORWARDS_LENGTH_TOLERANCE = 0.03; //in meters
            public static final double VELOCITY_TOLERANCE = 0.02;

            public static final double kS = 0.1;
            public static final double kG = 0.85;

            public static final double MAX_ACCELERATION = 2.5;
            public static final double MAX_VELOCITY = 0.6;
            public static final TrapezoidProfile.Constraints CONSTRAINTS = new TrapezoidProfile.Constraints(MAX_VELOCITY, MAX_ACCELERATION);

            public static final GBSparkMax.SparkMaxConfObject EXTENDER_CONFIG_OBJECT = new GBSparkMax.SparkMaxConfObject()
                    .withPID(RobotMap.TelescopicArm.Extender.PID)
                    .withPositionConversionFactor(RobotMap.TelescopicArm.Extender.POSITION_CONVERSION_FACTOR)
                    .withVelocityConversionFactor(RobotMap.TelescopicArm.Extender.VELOCITY_CONVERSION_FACTOR)
                    .withIdleMode(CANSparkMax.IdleMode.kBrake)
                    .withRampRate(General.RAMP_RATE_VAL)
                    .withCurrentLimit(30)
                    .withVoltageComp(General.VOLTAGE_COMP_VAL);
        }

        public static class Claw {
            public static final int MOTOR_ID = 2;
            public static final int SOLENOID_OPEN_CLAW_ID = 4;
            public static final int SOLENOID_CLOSED_CLAW_ID = 0;
            public static final double MOTOR_POWER_GRIP = 0.3;

            public static final double MOTOR_POWER_CONE = 0.6;
            public static final double MOTOR_POWER_RELEASE = -0.15;

            public static final double TIME_OF_GRIP_CONSTANT = 2;

            public static final GBSparkMax.SparkMaxConfObject CLAW_CONFIG_OBJECT = new GBSparkMax.SparkMaxConfObject()
                    .withPID(RobotMap.TelescopicArm.Extender.PID)
                    .withPositionConversionFactor(RobotMap.TelescopicArm.Extender.POSITION_CONVERSION_FACTOR)
                    .withVelocityConversionFactor(RobotMap.TelescopicArm.Extender.VELOCITY_CONVERSION_FACTOR)
                    .withIdleMode(CANSparkMax.IdleMode.kBrake)
                    .withRampRate(General.RAMP_RATE_VAL)
                    .withCurrentLimit(30)
                    .withInverted(true)
                    .withVoltageComp(General.VOLTAGE_COMP_VAL);
        }

        public static class Elbow {
            public static final int MOTOR_ID = 1;
            public static final double GEAR_RATIO = 1;

            public static final double kS = 0.133000000000002;
            public static final double MIN_Kg = 0.29;
            public static final double MAX_Kg = 0.647;
            public static final double MAX_KG_MEASUREMENT_LENGTH = 0.822964668273926;
            public static final double STARTING_ANGLE_RELATIVE_TO_GROUND = -1.765; //this is most easily measured using the encoder, so it is already radians
            public static final double MAX_ACCELERATION = 3;//actual max accel is 2.472 but this limits speed
            public static final double MAX_VELOCITY = 2.19;
            public static final TrapezoidProfile.Constraints CONSTRAINTS = new TrapezoidProfile.Constraints(MAX_VELOCITY, MAX_ACCELERATION);
            public static final PIDObject PID = new PIDObject().withKp(1.0).withMaxPower(1);
            public static final double STARTING_WALL_ZONE_ANGLE = Units.degreesToRadians(11);
            public static final double END_WALL_ZONE_ANGLE = Units.degreesToRadians(35.5);


            public static final double POSITION_CONVERSION_FACTOR = General.Motors.SPARKMAX_TICKS_PER_RADIAN / GEAR_RATIO;
            public static final double VELOCITY_CONVERSION_FACTOR = POSITION_CONVERSION_FACTOR;

            public static final double FORWARD_ANGLE_LIMIT = 2.13 + Math.toRadians(10);
            public static final double BACKWARD_ANGLE_LIMIT = Units.degreesToRadians(4);

            public static final double ANGLE_TOLERANCE = Units.degreesToRadians(3);
            public static final double ANGULAR_VELOCITY_TOLERANCE = Units.degreesToRadians(3);


            public static final GBSparkMax.SparkMaxConfObject ELBOW_CONFIG_OBJECT = new GBSparkMax.SparkMaxConfObject()
                    .withPID(PID)
                    .withIdleMode(CANSparkMax.IdleMode.kBrake)
                    .withRampRate(General.RAMP_RATE_VAL)
                    .withCurrentLimit(RobotMap.TelescopicArm.Elbow.CURRENT_LIMIT)
                    .withVoltageComp(General.VOLTAGE_COMP_VAL);


            public static final int CURRENT_LIMIT = 40;

        }
    }

    public static class ThirdLeg {
        public static final int FORWARD_PORT = 2;
        public static final int REVERSE_PORT = 3;
    }

    public static class Intake {
        public static final int ROLLER_ID = 4;
        public static final double DEFAULT_POWER = 1;
        public static final int BEAM_BREAKER_ID = 20;
        public static final GBSparkMax.SparkMaxConfObject INTAKE_CONFIG_OBJECT = new GBSparkMax.SparkMaxConfObject()
                .withPID(RobotMap.TelescopicArm.Extender.PID)
                .withPositionConversionFactor(RobotMap.TelescopicArm.Extender.POSITION_CONVERSION_FACTOR)
                .withVelocityConversionFactor(RobotMap.TelescopicArm.Extender.VELOCITY_CONVERSION_FACTOR)
                .withIdleMode(CANSparkMax.IdleMode.kBrake)
                .withRampRate(General.RAMP_RATE_VAL)
                .withCurrentLimit(30)
                .withInverted(true)
                .withVoltageComp(General.VOLTAGE_COMP_VAL);

        public static class Solenoid {
            public static final int FORWARD_PORT = 5;
            public static final int REVERSE_PORT = 2;
        }
    }
}