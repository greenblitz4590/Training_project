package edu.greenblitz.tobyDetermined.f2022;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxPIDController;
import edu.greenblitz.utils.PIDObject;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class Shooter extends GBSubsystem {
	
	private static Shooter instance;
	
	private CANSparkMax leader;
	private boolean preparedToShoot;
	private boolean isShooter;
	private static final double RPM = 3000;
	
	private Shooter() {
		leader = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
		
		leader.setInverted(true);
		
		leader.setIdleMode(CANSparkMax.IdleMode.kCoast);
		
		leader.setSmartCurrentLimit(40);
		
		leader.setClosedLoopRampRate(1);
		
		preparedToShoot = false;
	}
	
	public static void init() {
		instance = new Shooter();
	} //TODO why static?
	
	public static Shooter getInstance() {
		if (instance == null) {
			init();
		}
		return instance;
	}
	
	
	public void shoot(double power) {
		this.leader.set(power);
	}
	
	public void setIdleMode(CANSparkMax.IdleMode idleMode) {
		leader.setIdleMode(idleMode);
	}
	
	public void setSpeedByPID(double target) {
		leader.getPIDController().setReference(target, CANSparkMax.ControlType.kVelocity);
	}
	
	public void setPIDConsts(PIDObject obj, double iZone) {
		SparkMaxPIDController controller = leader.getPIDController();
		controller.setP(obj.getKp());
		controller.setI(obj.getKi());
		controller.setD(obj.getKd());
		controller.setFF(obj.getKf());
		controller.setIZone(iZone);
	}
	
	public void setPIDConsts(PIDObject obj) {
		setPIDConsts(obj, 0);
	}
	
	public double getShooterSpeed() {
		return leader.getEncoder().getVelocity();
	}
	
	public void resetEncoder() {
		leader.getEncoder().setPosition(0);
	}
	
	public boolean isPreparedToShoot() {
		return preparedToShoot;
	}
	
	public void setPreparedToShoot(boolean preparedToShoot) {
		this.preparedToShoot = preparedToShoot;
	}
	
	
	public SparkMaxPIDController getPIDController() {
		return leader.getPIDController();
	}
	
	public void toCoast() {
		this.leader.setIdleMode(CANSparkMax.IdleMode.kCoast);
	}
}
