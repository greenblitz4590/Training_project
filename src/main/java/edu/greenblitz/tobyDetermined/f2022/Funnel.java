package edu.greenblitz.tobyDetermined.f2022;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class Funnel extends GBSubsystem {
		private final CANSparkMax motor;
	
	private static Funnel instance;
	
	public static Funnel getInstance() {
		if (instance == null) {
			instance = new Funnel();
		}
		return instance;
	}
	
	protected Funnel() {
		motor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushed);
		motor.setInverted(false);
	}
	
	public void moveMotor(double power) {
		motor.set(power);
	}
	
	public void
	moveMotor(boolean reversed) {
		moveMotor(reversed ? -0.7 : 0.7);
	}
	
	public void moveMotor() {
		moveMotor(false);
	}
	
	public void stopMotor() {
		motor.set(0);
	}
}
