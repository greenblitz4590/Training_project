package edu.greenblitz.tobyDetermined.f2022;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class Gripper extends GBSubsystem{
	
	private static Gripper instance;
	
	private CANSparkMax leader;
	
	
	public static Gripper getInstance(){
		if(instance == null){
			instance = new Gripper();
		}
		return instance;
	}
	
	private Gripper(){
		this.leader = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushed);
	}
	
	
	public void setPower(double power){
		leader.set(power);
	}
	
	public void stop(){
		setPower(0);
	}
	
	
}
