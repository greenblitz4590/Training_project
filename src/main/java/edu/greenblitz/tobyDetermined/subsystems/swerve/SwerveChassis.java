package edu.greenblitz.tobyDetermined.subsystems.swerve;

import com.revrobotics.CANSparkMax;
import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.subsystems.GBSubsystem;
import edu.greenblitz.tobyDetermined.subsystems.Limelight.MultiLimelight;
import edu.greenblitz.tobyDetermined.subsystems.Photonvision;
import edu.greenblitz.utils.PigeonGyro;
import edu.greenblitz.utils.PitchRollAdder;
import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.photonvision.EstimatedRobotPose;

import java.util.Optional;

public class SwerveChassis extends GBSubsystem {
	
	private static SwerveChassis instance;
	private final SwerveModule frontRight, frontLeft, backRight, backLeft;
	private final PigeonGyro pigeonGyro;
	private final SwerveDriveKinematics kinematics;
	private final SwerveDrivePoseEstimator poseEstimator;
	private final Field2d field = new Field2d();
	
	private final Ultrasonic ultrasonic;
	private final int FILTER_BUFFER_SIZE = 15;
	Debouncer encoderBrokenDebouncer = new Debouncer(0.2);
	
	
	public static final double TRANSLATION_TOLERANCE = 0.05;
	public static final double ROTATION_TOLERANCE = 2;
	private boolean doVision;
	
	public SwerveChassis() {
		this.frontLeft = new SdsSwerveModule(RobotMap.Swerve.SdsModuleFrontLeft);
		this.frontRight = new SdsSwerveModule(RobotMap.Swerve.SdsModuleFrontRight);
		this.backLeft = new SdsSwerveModule(RobotMap.Swerve.SdsModuleBackLeft);
		this.backRight = new SdsSwerveModule(RobotMap.Swerve.SdsModuleBackRight);
		this.ultrasonic = new Ultrasonic(RobotMap.Ultrasonic.PING_DIO_PORT, RobotMap.Ultrasonic.ECHO_DIO_PORT);
		Ultrasonic.setAutomaticMode(true);
		doVision = true;
		
		
		this.pigeonGyro = new PigeonGyro(RobotMap.gyro.pigeonID);
		
		this.kinematics = new SwerveDriveKinematics(
				RobotMap.Swerve.SwerveLocationsInSwerveKinematicsCoordinates
		);
		this.poseEstimator = new SwerveDrivePoseEstimator(this.kinematics,
				getPigeonAngle(),
				getSwerveModulePositions(),
				new Pose2d(new Translation2d(), new Rotation2d()),//Limelight.getInstance().estimateLocationByVision(),
				new MatBuilder<>(Nat.N3(), Nat.N1()).fill(RobotMap.Vision.STANDARD_DEVIATION_ODOMETRY, RobotMap.Vision.STANDARD_DEVIATION_ODOMETRY, RobotMap.Vision.STANDARD_DEVIATION_ODOMETRY),
				new MatBuilder<>(Nat.N3(), Nat.N1()).fill(RobotMap.Vision.STANDARD_DEVIATION_VISION2D, RobotMap.Vision.STANDARD_DEVIATION_VISION2D, RobotMap.Vision.STANDARD_DEVIATION_VISION_ANGLE));
		SmartDashboard.putData("field", getField());
		SmartDashboard.putData("field", getField());
	}
	
	
	public static SwerveChassis getInstance() {
		init();
		return instance;
	}
	
	
	public static void init() {
		if (instance == null) {
			instance = new SwerveChassis();
		}
	}
	
	@Override
	public void periodic() {
		
		updatePoseEstimationLimeLight();
		field.setRobotPose(getRobotPose());
	}
	
	public void resetAll(Pose2d pose) {
		poseEstimator.resetPosition(getPigeonAngle(), getSwerveModulePositions(), pose);
	}
	
	/**
	 * @return returns the swerve module based on its name
	 */
	public SwerveModule getModule(Module module) { //TODO make private
		switch (module) {
			case BACK_LEFT:
				return backLeft;
			case BACK_RIGHT:
				return backRight;
			case FRONT_LEFT:
				return frontLeft;
			case FRONT_RIGHT:
				return frontRight;
		}
		return null;
	}
	
	/**
	 * stops all the modules (power(0))
	 */
	public void stop() {
		frontRight.stop();
		frontLeft.stop();
		backRight.stop();
		backLeft.stop();
	}
	
	/**
	 * resetting all the angle motor's encoders to 0
	 */
	public void resetEncodersByCalibrationRod() {
		getModule(Module.FRONT_LEFT).resetEncoderToValue(0);
		getModule(Module.FRONT_RIGHT).resetEncoderToValue(0);
		getModule(Module.BACK_LEFT).resetEncoderToValue(0);
		getModule(Module.BACK_RIGHT).resetEncoderToValue(0);
	}
	
	public void resetAllEncoders() {
		getModule(Module.FRONT_LEFT).resetEncoderByAbsoluteEncoder(Module.FRONT_LEFT);
		getModule(Module.FRONT_RIGHT).resetEncoderByAbsoluteEncoder(Module.FRONT_RIGHT);
		getModule(Module.BACK_LEFT).resetEncoderByAbsoluteEncoder(Module.BACK_LEFT);
		getModule(Module.BACK_RIGHT).resetEncoderByAbsoluteEncoder(Module.BACK_RIGHT);
		
	}
	
	/**
	 * get the absolute encoder value of a specific module
	 */
	public double getModuleAbsoluteEncoderValue(Module module) {
		return getModule(module).getAbsoluteEncoderValue();
	}
	
	
	/**
	 * all code below is self-explanatory - well, after a long time It's maybe not self-explanatory
	 * <p>
	 * ALL IN RADIANS, NOT DEGREES
	 */
	
	public double getModuleAngle(Module module) {
		return getModule(module).getModuleAngle();
	}
	
	public boolean moduleIsAtAngle(Module module, double targetAngleInRads, double errorInRads) {
		return getModule(module).isAtAngle(targetAngleInRads, errorInRads);
	}
	
	public void resetChassisPose() {
		pigeonGyro.setYaw(0);
		poseEstimator.resetPosition(getPigeonAngle(), getSwerveModulePositions(), new Pose2d());
	}
	
	/**
	 * returns chassis angle in radians
	 */
	private Rotation2d getPigeonAngle() {
		return new Rotation2d(pigeonGyro.getYaw());
	}
	
	public double getChassisAngle() {
		return getRobotPose().getRotation().getRadians();
		
	}
	
	/**
	 * setting module states to all 4 modules
	 */
	public void setModuleStates(SwerveModuleState[] states) {
		setModuleStateForModule(Module.FRONT_LEFT,
				SwerveModuleState.optimize(states[0], new Rotation2d(getModuleAngle(Module.FRONT_LEFT))));
		setModuleStateForModule(Module.FRONT_RIGHT,
				SwerveModuleState.optimize(states[1], new Rotation2d(getModuleAngle(Module.FRONT_RIGHT))));
		setModuleStateForModule(Module.BACK_LEFT,
				SwerveModuleState.optimize(states[2], new Rotation2d(getModuleAngle(Module.BACK_LEFT))));
		setModuleStateForModule(Module.BACK_RIGHT,
				SwerveModuleState.optimize(states[3], new Rotation2d(getModuleAngle(Module.BACK_RIGHT))));
	}
	
	public void moveByChassisSpeeds(double forwardSpeed, double leftwardSpeed, double angSpeed, double currentAng) {
		ChassisSpeeds chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
				forwardSpeed,
				leftwardSpeed,
				angSpeed,
				Rotation2d.fromDegrees(Math.toDegrees(currentAng)));
		SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassisSpeeds);
		SwerveModuleState[] desaturatedStates = desaturateSwerveModuleStates(states);
		setModuleStates(desaturatedStates);
	}
	
	/**
	 * makes sure no module is requested to move faster than possible by linearly scaling all module velocities to comply with the constraint
	 *
	 * @param states original velocity states computed from the kinematics
	 * @return states that create the same ratio between speeds but scaled down
	 */
	private static SwerveModuleState[] desaturateSwerveModuleStates(SwerveModuleState[] states) {
		double desaturationFactor = 1;
		for (SwerveModuleState state : states) {
			desaturationFactor = Math.max(desaturationFactor, state.speedMetersPerSecond / RobotMap.Swerve.MAX_VELOCITY);
		}
		SwerveModuleState[] desaturatedStates = new SwerveModuleState[states.length];
		for (int i = 0; i < states.length; i++) {
			desaturatedStates[i] = new SwerveModuleState(states[i].speedMetersPerSecond / desaturationFactor, states[i].angle);
		}
		return desaturatedStates;
	}
	
	public ChassisSpeeds getChassisSpeeds() {
		return kinematics.toChassisSpeeds(getModuleState(Module.FRONT_LEFT),
				getModuleState(Module.FRONT_RIGHT),
				getModuleState(Module.BACK_LEFT),
				getModuleState(Module.BACK_RIGHT));
	}
	
	public SwerveModulePosition[] getSwerveModulePositions() {
		return new SwerveModulePosition[]{
				frontLeft.getCurrentPosition(),
				frontRight.getCurrentPosition(),
				backLeft.getCurrentPosition(),
				backRight.getCurrentPosition()
		};
	}
	
	public SwerveDriveKinematics getKinematics() {
		return this.kinematics;
	}
	
	public PigeonGyro getPigeonGyro() {
		return pigeonGyro;
	}
	
	/**
	 * moving a single module by module state
	 */
	private void setModuleStateForModule(Module module, SwerveModuleState state) {
		getModule(module).setModuleState(state);
	}
	
	/**
	 * rotates chassis by percentOutput [-1, 1]
	 */
	public void rotateChassisByPower(double percentOutput) {
		for (int i = 0; i < Module.values().length; i++) {
			Translation2d translationFromCenter = RobotMap.Swerve.SwerveLocationsInSwerveKinematicsCoordinates[i];
			getModule(Module.values()[i]).rotateToAngle(Math.atan2(translationFromCenter.getY(), translationFromCenter.getX()) + Math.PI * 0.5);
			getModule(Module.values()[i]).setLinPowerOnlyForCalibrations(percentOutput);
		}
	}
	
	/**
	 * for calibration purposes
	 */
	public void rotateModuleByPower(Module module, double power) {
		getModule(module).setRotPowerOnlyForCalibrations(power);
	}
	
	public void updatePoseEstimationPhotonVision() {
		poseEstimator.update(getPigeonAngle(), getSwerveModulePositions());
		Photonvision.getInstance().getUpdatedPoseEstimator().ifPresent((EstimatedRobotPose pose) -> poseEstimator.addVisionMeasurement(pose.estimatedPose.toPose2d(), pose.timestampSeconds));
	}
	
	public void updatePoseEstimationLimeLight() {
		poseEstimator.update(getPigeonAngle(), getSwerveModulePositions());
		if (doVision) {
			for (Optional<Pair<Pose2d, Double>> target : MultiLimelight.getInstance().getAllEstimates()) {
				target.ifPresent(this::addVisionMeasurement);
			}
		}
	}
	
	private void addVisionMeasurement(Pair<Pose2d, Double> poseTimestampPair) {
		Pose2d visionPose = poseTimestampPair.getFirst();
		double limelightXSpeed = SwerveChassis.getInstance().getChassisSpeeds().vxMetersPerSecond * RobotMap.Vision.VISION_CONSTANT;
		double limelightYSpeed = SwerveChassis.getInstance().getChassisSpeeds().vyMetersPerSecond * RobotMap.Vision.VISION_CONSTANT;
		double limelightRotationSpeed = SwerveChassis.getInstance().getChassisSpeeds().omegaRadiansPerSecond * RobotMap.Vision.VISION_CONSTANT;
		if (!(visionPose.getTranslation().getDistance(SwerveChassis.getInstance().getRobotPose().getTranslation()) > RobotMap.Vision.MIN_DISTANCE_TO_FILTER_OUT)) {
			poseEstimator.setVisionMeasurementStdDevs(new MatBuilder<>(Nat.N3(), Nat.N1()).fill(limelightXSpeed, limelightYSpeed, RobotMap.Vision.STANDARD_DEVIATION_VISION_ANGLE));
			poseEstimator.addVisionMeasurement(visionPose, poseTimestampPair.getSecond());
		}
	}
	
	public Pose2d getRobotPose() {
		return poseEstimator.getEstimatedPosition();
	}
	
	public void resetToVision() {
		Optional<Pair<Pose2d, Double>> visionOutput = MultiLimelight.getInstance().getFirstAvailableTarget();
		if(visionOutput.isPresent()) {
			poseEstimator.setVisionMeasurementStdDevs(new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0, 0, 0));
			visionOutput.ifPresent((pose2dDoublePair) -> resetChassisPose(pose2dDoublePair.getFirst()));
			poseEstimator.setVisionMeasurementStdDevs(new MatBuilder<>(Nat.N3(), Nat.N1()).fill(RobotMap.Vision.STANDARD_DEVIATION_VISION2D, RobotMap.Vision.STANDARD_DEVIATION_VISION2D, RobotMap.Vision.STANDARD_DEVIATION_VISION_ANGLE));
		}
		}
	
	public boolean isAtPose(Pose2d goalPose) {
		Pose2d robotPose = getRobotPose();
		
		//is translation difference beneath tolerance
		boolean isAtX = Math.abs(goalPose.getX() - robotPose.getX()) <= TRANSLATION_TOLERANCE;
		boolean isAtY = Math.abs(goalPose.getY() - robotPose.getY()) <= TRANSLATION_TOLERANCE;
		
		//is angle difference beneath tolerance from both directions
		Rotation2d angDifference = (goalPose.getRotation().minus(robotPose.getRotation()));
		boolean isAtAngle = angDifference.getRadians() <= ROTATION_TOLERANCE
				|| (Math.PI * 2) - angDifference.getRadians() <= ROTATION_TOLERANCE;
		
		return isAtAngle && isAtX && isAtY;
	}
	
	public Sendable getField() {
		return field;
	}
	
	public SwerveModuleState getModuleState(Module module) {
		return getModule(module).getModuleState();
	}
	
	public enum Module {
		FRONT_LEFT,
		FRONT_RIGHT,
		BACK_LEFT,
		BACK_RIGHT
	}
	
	public boolean moduleIsAtAngle(Module module, double errorInRads) {
		return getModule(module).isAtAngle(errorInRads);
	}
	
	public void resetChassisPose(Pose2d pose) {
		poseEstimator.resetPosition(getPigeonAngle(), getSwerveModulePositions(), pose);
	}
	
	public void moveByChassisSpeeds(ChassisSpeeds chassisSpeeds) {
		moveByChassisSpeeds(chassisSpeeds.vxMetersPerSecond,
				chassisSpeeds.vyMetersPerSecond,
				chassisSpeeds.omegaRadiansPerSecond,
				getChassisAngle()
		);
		SmartDashboard.putNumber("omega", chassisSpeeds.omegaRadiansPerSecond);
	}
	
	public double getUltrasonicDistance() {
		MedianFilter filter = new MedianFilter(FILTER_BUFFER_SIZE);
		return filter.calculate(ultrasonic.getRangeMM());
	}
	
	/**
	 * set the idle mode of the linear motor to brake
	 */
	public void setIdleModeBrake() {
		for (Module module : Module.values()) {
			getModule(module).setLinIdleModeBrake();
		}
	}
	
	public void setIdleModeCoast() {
		for (Module module : Module.values()) {
			getModule(module).setLinIdleModeCoast();
		}
	}
	
	public void setAngleMotorsIdleMode(CANSparkMax.IdleMode idleMode) {
		if (idleMode == CANSparkMax.IdleMode.kBrake) {
			for (Module module : Module.values()) {
				getModule(module).setRotIdleModeBrake();
			}
		} else {
			for (Module module : Module.values()) {
				getModule(module).setRotIdleModeCoast();
			}
		}
	}
	
	public double getAngleToGround(){
		return PitchRollAdder.add(pigeonGyro.getRoll(), pigeonGyro.getPitch());
	}
	
	public boolean isEncoderBroken(Module module){
		return getModule(module).isEncoderBroken();
	}
	
	public boolean isEncoderBroken(){
		boolean broken = false;
		for (Module module: Module.values()) {
			broken |= isEncoderBroken(module);
		}
		return broken;
	}

	public void disableVision(){
		doVision = false;
	}

	public void enableVision(){
		doVision = true;
	}

}