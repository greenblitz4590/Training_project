package edu.greenblitz.tobyDetermined.f2022;

import edu.greenblitz.utils.GBCommand;

public class ShootByConstant extends GBCommand {
	
	protected double power;
	
	public ShootByConstant(double power) {
		this.power = power;
		require(Shooter.getInstance());
		
	}
	
	@Override
	public void initialize() {
		super.initialize();
	}
	
	@Override
	public void execute() {
		Shooter.getInstance().shoot(power);
	}
	
	@Override
	public void end(boolean interrupted) {
		Shooter.getInstance().shoot(0);
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
	
}
